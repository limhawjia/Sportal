package wjhj.orbital.sportsmatchfindingapp.repo;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import java9.util.stream.StreamSupport;
import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.game.TimeOfDay;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public class SportalRepo implements ISportalRepo {
    private static final String DATA_DEBUG = "SportalRepo";
    private static final String USERS_PATH = "Users";
    private static final String GAMES_PATH = "Games";

    private final FirebaseFirestore db;
    private final LoadingCache<String, LiveData<UserProfile>> mUserProfilesCache;
    private final LoadingCache<String, LiveData<Game>> mGamesCache;

    private static SportalRepo instance;

    public static SportalRepo getInstance() {
        if (instance == null) {
            synchronized (SportalRepo.class) {
                if (instance == null) {
                    instance = new SportalRepo();
                }
            }
        }
        return instance;
    }

    private SportalRepo() {
        db = FirebaseFirestore.getInstance();
        mUserProfilesCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, LiveData<UserProfile>>() {
                    @Override
                    public LiveData<UserProfile> load(@NonNull String key) {
                        DocumentReference ref = db.collection(USERS_PATH).document(key);
                        return Transformations.map(convertToLiveData(ref, UserProfileDataModel.class),
                                SportalRepo.this::toUserProfile);
                    }
                });
        mGamesCache = CacheBuilder.newBuilder()
                .maximumSize(200)
                .expireAfterAccess(20, TimeUnit.MINUTES)
                .build(new CacheLoader<String, LiveData<Game>>() {
                    @Override
                    public LiveData<Game> load(@NonNull String key) {
                        DocumentReference ref = db.collection(GAMES_PATH).document(key);
                        return Transformations.map(convertToLiveData(ref, GameDataModel.class),
                                SportalRepo.this::toGame);
                    }
                });
    }

    @Override
    public Task<Void> addUser(String uid, UserProfile userProfile) {
        UserProfileDataModel dataModel = toUserProfileDataModel(userProfile);

        return db.collection(USERS_PATH)
                .document(uid)
                .set(dataModel)
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, uid + " added"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, uid + " add failed", e));
    }

    @Override
    public Task<Void> updateUser(String uid, UserProfile userProfile) {
        UserProfileDataModel dataModel = toUserProfileDataModel(userProfile);
        return db.collection(USERS_PATH)
                .document(uid)
                .set(dataModel, SetOptions.merge());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Task<Boolean> isProfileSetUp(String uid) {
        return db.collection(USERS_PATH)
                .document(uid)
                .get()
                .continueWith(task -> task.getResult().exists());
    }

    @Override
    public LiveData<UserProfile> getUser(String userUid) {
        return mUserProfilesCache.getUnchecked(userUid);
    }

    @Override
    public LiveData<List<UserProfile>> selectUsersStartingWith(String field, String queryText) {
        LiveData<List<UserProfileDataModel>> listLiveData = convertToLiveData(
                queryStartingWith(USERS_PATH, field, queryText), UserProfileDataModel.class);

        return Transformations.map(listLiveData, this::toUserProfiles);
    }

    @Override
    public Task<Void> deleteUser(String userUid) {
        return deleteDocument(userUid, USERS_PATH);
    }

    // Should be called when building a game to get the uid for the game.
    @Override
    public String generateGameUid() {
        return db.collection(GAMES_PATH)
                .document()
                .getId();
    }

    @Override
    public Task<Void> addGame(String gameUid, Game game) {
        GameDataModel dataModel = toGameDataModel(game);

        WriteBatch batch = db.batch();

        DocumentReference gameDocRef = db.collection(GAMES_PATH).document(gameUid);
        batch.set(gameDocRef, dataModel);

        CollectionReference users = db.collection(USERS_PATH);
        DocumentReference creatorDocRef = users.document(game.getCreatorUid());
        batch.update(creatorDocRef, "games.pending", FieldValue.arrayUnion(gameUid));

        for (String participantUid : game.getParticipatingUids()) {
            DocumentReference participantDocRef = users.document(participantUid);
            batch.update(participantDocRef, "games.pending", FieldValue.arrayUnion(gameUid));
        }

        return batch.commit()
                .addOnSuccessListener(docRef -> Log.d(DATA_DEBUG, "Add game complete."))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game failed.", e));
    }

    @Override
    public Task<Void> updateGame(String gameId, Game game) {
        GameDataModel dataModel = toGameDataModel(game);
        return db.collection(GAMES_PATH)
                .document(gameId)
                .set(dataModel, SetOptions.merge());
    }

    @Override
    public LiveData<Game> getGame(String gameID) {
        return mGamesCache.getUnchecked(gameID);
    }

    @Override
    public LiveData<Map<String, Game>> getGamesWithFilters(GameSearchFilter filter) {
        Log.d("hi", "Filtering started...");
        CollectionReference gamesRef = FirebaseFirestore.getInstance().collection("Games");
        String nameQuery = null;

        HashMap<String, Game> allGames = new HashMap<>();
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();

        if (filter.getNameQuery() != null && filter.getNameQuery().length() > 3) {
            nameQuery = filter.getNameQuery();
        }

        List<Sport> sportsQuery = filter.getSportQuery();
        List<TimeOfDay> timeOfDayQuery = filter.getTimeOfDayQuery();
        if (timeOfDayQuery.isEmpty()) {
            timeOfDayQuery = Arrays.asList(TimeOfDay.MORNING, TimeOfDay.AFTERNOON, TimeOfDay.NIGHT);
        }
        List<Difficulty> skillLevelQuery = filter.getSkillLevelQuery();
        if (skillLevelQuery.isEmpty()) {
            skillLevelQuery =
                    Arrays.asList(Difficulty.BEGINNER, Difficulty.INTERMEDIATE, Difficulty.ADVANCED);
        }

        MutableLiveData<Map<String, Game>> data = new MutableLiveData<>();
        for (Sport sport :sportsQuery) {
            for (TimeOfDay timeOfDay : timeOfDayQuery) {
                for (Difficulty skillLevel : skillLevelQuery) {
                    Query query = gamesRef
                            .orderBy("time")
                            .startAt(timeOfDay.getStartTime().toString())
                            .endAt(timeOfDay.getEndTime().toString())
                            .whereEqualTo("sport", sport.toString().toUpperCase())
                            .whereEqualTo("skillLevel", skillLevel.toString().toUpperCase())
                            .limit(20);
                    if (nameQuery != null) {
                        query = query.whereArrayContains("nameSubstrings", nameQuery);
                    }
                    Task<QuerySnapshot> querySnapshotTask = query.get();
                    querySnapshotTask.addOnSuccessListener(snapshots -> {
                        StreamSupport.stream(snapshots.getDocuments())
                                .map(documentSnapshot -> documentSnapshot.toObject(GameDataModel.class))
                                .map(this::toGame)
                                .forEach(game -> {
                                    Log.d("hi", "Adding game " + game.toString());
                                    allGames.put(game.getUid(), game);
                                    data.setValue(allGames);
                                });
                    });
                }
            }
        }

        return data;
    }

    @Override
    public LiveData<List<Game>> selectGamesStartingWith(String field, String queryText) {
        LiveData<List<GameDataModel>> listLiveData = convertToLiveData(
                queryStartingWith("Games", field, queryText), GameDataModel.class);

        return Transformations.map(listLiveData, this::toGames);
    }

    @Override
    public Task<Void> deleteGame(String gameId) {
        return deleteDocument(gameId, "Games");
    }

    // HELPER METHOD
    private <T> LiveData<T> convertToLiveData(DocumentReference docRef, Class<T> valueType) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        docRef.addSnapshotListener((value, err) -> {
            if (err != null) {
                Log.d(DATA_DEBUG, "database snapshot error", err);
            } else if (value.exists()) {
                try {
                    liveData.postValue(value.toObject(valueType));
                } catch (RuntimeException e) {
                    Log.d(DATA_DEBUG, "deserialization error", e);
                }
            } else {
                Log.d(DATA_DEBUG, "document does not exist");
            }
        });
        return liveData;
    }

    private <T> LiveData<List<T>> convertToLiveData(Query query, Class<T> valueType) {
        MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        query.addSnapshotListener((value, err) -> {
            if (err != null) {
                Log.d(DATA_DEBUG, "database snapshot error", err);
            } else {
                assert value != null;
                liveData.postValue(value.toObjects(valueType));
            }
        });
        return liveData;
    }

    private Query queryStartingWith(String collection, String field, String queryText) {
        return db.collection(collection)
                .orderBy(field)
                .startAt(queryText)
                .endAt(queryText + "\uf8ff"); // StackOverflow hacks...
    }

    private Task<Void> deleteDocument(String docID, String collectionPath) {
        return db.collection(collectionPath)
                .document(docID)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, docID + " successfully deleted!"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Error deleting document", e));
    }

    // METHODS TO CONVERT BETWEEN DOMAIN MODEL AND DATA MODEL
    private UserProfileDataModel toUserProfileDataModel(UserProfile userProfile) {
        return new UserProfileDataModel(userProfile);
    }

    private UserProfile toUserProfile(UserProfileDataModel dataModel) {
        Map<GameStatus, List<String>> newMap = new HashMap<>();
        Map<String, List<String>> oldMap = dataModel.getGames();
        for (GameStatus gs : GameStatus.values()) {
            List<String> games = oldMap.get(gs.toString());
            if (games != null) {
                newMap.put(gs, games);
            } else {
                newMap.put(gs, new ArrayList<>());
            }
        }

        List<String> friendUids = dataModel.getFriendUids() == null
                ? new ArrayList<>()
                : dataModel.getFriendUids();

        List<Sport> preferences = dataModel.getPreferences() == null
                ? new ArrayList<>()
                : dataModel.getPreferences();

        return UserProfile.builder()
                .withDisplayName(dataModel.getDisplayName())
                .withGender(dataModel.getGender())
                .withBirthday(LocalDate.parse(dataModel.getBirthday()))
                .withCountry(dataModel.getCountry())
                .withUid(dataModel.getUid())
                .withDisplayPicUri(Uri.parse(dataModel.getDisplayPicUri()))
                .withBio(Optional.fromNullable(dataModel.getBio()))
                .addAllFriendUids(friendUids)
                .addAllPreferences(preferences)
                .putAllGames(newMap)
                .build();
    }

    private List<UserProfile> toUserProfiles(List<UserProfileDataModel> dataModels) {
        List<UserProfile> newList = new ArrayList<>();
        for (UserProfileDataModel dataModel : dataModels) {
            newList.add(toUserProfile(dataModel));
        }
        return newList;
    }

    private GameDataModel toGameDataModel(Game game) {
        return new GameDataModel(game);
    }

    private Game toGame(GameDataModel dataModel) {

        return Game.builder()
                .withGameName(dataModel.getGameName())
                .withSport(dataModel.getSport())
                .withLocation(dataModel.getLocation())
                .withPlaceName(dataModel.getPlaceName())
                .withMinPlayers(dataModel.getMinPlayers())
                .withMaxPlayers(dataModel.getMaxPlayers())
                .withSkillLevel(dataModel.getSkillLevel())
                .withDate(LocalDate.parse(dataModel.getDate()))
                .withTime(LocalTime.parse(dataModel.getTime()))
                .withDuration(Duration.parse(dataModel.getDuration()))
                .withUid(dataModel.getUid())
                .withCreatorUid(dataModel.getCreatorUid())
                .withDescription(Optional.fromNullable(dataModel.getDescription()))
                .addAllParticipatingUids(dataModel.getParticipatingUids())
                .build();
    }

    private List<Game> toGames(List<GameDataModel> dataModels) {
        List<Game> newList = new ArrayList<>();
        for (GameDataModel dataModel : dataModels) {
            newList.add(toGame(dataModel));
        }
        return newList;
    }

    private void test() {
        GeoFirestore geoFirestore = new GeoFirestore(db.collection("lul"));
    }
}
