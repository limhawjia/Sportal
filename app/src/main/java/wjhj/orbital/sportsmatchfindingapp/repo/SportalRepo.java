package wjhj.orbital.sportsmatchfindingapp.repo;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.base.Optional;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.mapbox.geojson.Point;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import java9.util.stream.Stream;
import java9.util.stream.StreamSupport;
import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.game.TimeOfDay;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public class SportalRepo implements ISportalRepo {
    private static final String DATA_DEBUG = "SportalRepo";

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
    }

    @Override
    public Task<Void> addUser(String uid, UserProfile userProfile) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserProfileDataModel dataModel = toUserProfileDataModel(userProfile);

        return db.collection("Users")
                .document(uid)
                .set(dataModel)
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, uid + " added"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, uid + " add failed", e));
    }

    @Override
    public Task<Void> updateUser(String uid, UserProfile userProfile) {
        UserProfileDataModel dataModel = toUserProfileDataModel(userProfile);
        return updateDocument(uid, "Users", dataModel);
    }

    @Override
    public LiveData<UserProfile> getUser(String userUid) {
        LiveData<UserProfileDataModel> dataModelLiveData = convertToLiveData(
                getDocumentFromCollection(userUid, "Users"), UserProfileDataModel.class);

        return Transformations.map(dataModelLiveData, this::toUserProfile);
    }

    @Override
    public LiveData<List<UserProfile>> selectUsersStartingWith(String field, String queryText) {
        LiveData<List<UserProfileDataModel>> listLiveData = convertToLiveData(
                queryStartingWith("Users", field, queryText), UserProfileDataModel.class);

        return Transformations.map(listLiveData, this::toUserProfiles);
    }

    @Override
    public LiveData<List<UserProfile>> selectUsersArrayContains(String field, String queryText) {
        LiveData<List<UserProfileDataModel>> listLiveData = convertToLiveData(
                queryArrayContains("Users", field, queryText), UserProfileDataModel.class);

        return Transformations.map(listLiveData, this::toUserProfiles);
    }

    @Override
    public Task<Void> deleteUser(String userUid) {
        return deleteDocument(userUid, "Users");
    }

    // Should be called when building a game to get the uid for the game.
    @Override
    public String generateGameUid() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("Games")
                .document()
                .getId();
    }

    @Override
    public Task<Void> addGame(String gameUid, Game game) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        GameDataModel dataModel = toGameDataModel(game);

        return db.collection("Games")
                .document(gameUid)
                .set(dataModel)
                .addOnSuccessListener(documentReference -> {
                    Log.d(DATA_DEBUG, "Add game complete.");
                    // If game added successfully, also add game to all users participating
                    addGameToUser(game.getCreatorUid(), gameUid);
                    for (String user : game.getParticipatingUids()) {
                        addGameToUser(user, gameUid);
                    }
                })
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game failed.", e));

    }

    @Override
    public Task<Void> updateGame(String gameId, Game game) {
        GameDataModel dataModel = toGameDataModel(game);
        return updateDocument(gameId, "Games", dataModel);
    }

    @Override
    public LiveData<Game> getGame(String gameID) {
        LiveData<GameDataModel> dataModelLiveData = convertToLiveData(
                getDocumentFromCollection(gameID, "Games"), GameDataModel.class);

        return Transformations.map(dataModelLiveData, this::toGame);
    }

    @Override
    public LiveData<Map<String, Game>> getGamesWithFilters(GameSearchFilter filter) {
        Log.d("hi", "Filtering started...");
        CollectionReference gamesRef = FirebaseFirestore.getInstance().collection("Games");
        String nameQuery;

        HashMap<String, Game> allGames = new HashMap<>();
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();

        nameQuery = null;
        if (filter.hasNameQuery()) {
            if (filter.getNameQuery().length() >= 3) {
                Log.d("hi", "Name query changed to " + filter.getNameQuery());
                nameQuery = filter.getNameQuery();
            }
        }


        if (filter.hasSportQuery()) {
            Log.d("hi", "Filtering by sports...");
            for (Sport sport : filter.getSportQuery()) {
                Log.d("hi", "Current sport: " + sport.toString());
                Query query = gamesRef.whereEqualTo("sport", sport.toString().toUpperCase());
                if (nameQuery != null) {
                    query = query.whereArrayContains("nameSubstrings", nameQuery);
                }
                Task<QuerySnapshot> querySnapshotTask = query.get();
                tasks.add(querySnapshotTask);
                Log.d("hi", "Added task " + sport.toString());
            }
        }

        if (filter.hasSkillLevelQuery()) {
            Log.d("hi", "Filtering by skill...");
            for (Difficulty skill : filter.getSkillLevelQuery()) {
                Log.d("hi", "Current skill: " + skill.toString());
                Query query = gamesRef.whereEqualTo("skillLevel", skill.toString().toUpperCase());
                if (nameQuery != null) {
                    query = query.whereArrayContains("nameSubstrings", nameQuery);
                }
                Task<QuerySnapshot> querySnapshotTask = query.get();
                tasks.add(querySnapshotTask);
            }
        }

        if (filter.hasTimeOfDayQuery()) {
            Log.d("hi", "Filtering by time...");
            for (TimeOfDay timeOfDay : filter.getTimeOfDayQuery()) {
                Log.d("hi", "Current time: " + timeOfDay.toString());
                Query query = FirebaseFirestore.getInstance().collection("Games")
                        .orderBy("time")
                        .startAt(timeOfDay.getStartTime())
                        .endAt(timeOfDay.getEndTime());
                if (nameQuery != null) {
                    query = query.whereArrayContains("nameSubstrings", nameQuery);
                }
                Task<QuerySnapshot> querySnapshotTask = query.get();
                tasks.add(querySnapshotTask);
            }
        }

        Task<List<Task<?>>> mediator = Tasks.whenAllComplete(tasks)
                .addOnSuccessListener(taskList -> {
                    StreamSupport.stream(taskList).forEach(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = (QuerySnapshot) task.getResult();
                            StreamSupport.stream(snapshot.getDocuments())
                                    .map(documentSnapshot -> documentSnapshot.toObject(GameDataModel.class))
                                    .map(this::toGame)
                                    .forEach(game -> {
                                        Log.d("hi", "Adding game " + game.toString());
                                        allGames.put(game.getUid(), game);
                                    });
                        }
                    });
                });

        MutableLiveData<Map<String, Game>> data = new MutableLiveData<>();
        mediator.addOnSuccessListener(result -> data.setValue(allGames));
        return data;
    }

    @Override
    public LiveData<List<Game>> selectGamesStartingWith(String field, String queryText) {
        LiveData<List<GameDataModel>> listLiveData = convertToLiveData(
                queryStartingWith("Games", field, queryText), GameDataModel.class);

        return Transformations.map(listLiveData, this::toGames);
    }

    @Override
    public LiveData<List<Game>> selectGamesArrayContains(String field, String queryText) {
        LiveData<List<GameDataModel>> listLiveData = convertToLiveData(
                queryArrayContains("Games", field, queryText), GameDataModel.class);

        return Transformations.map(listLiveData, this::toGames);
    }

    @Override
    public Task<Void> deleteGame(String gameId) {
        return deleteDocument(gameId, "Games");
    }

    // HELPER METHODS
    private void addGameToUser(String userUid, String gameID) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(userUid)
                .update("games.pending", FieldValue.arrayUnion(gameID))
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Added game to " + userUid))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game to " + userUid + " failed", e));
    }

    private Task<Void> updateDocument(String docId, String collectionPath, Object dataModel) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collectionPath)
                .document(docId)
                .set(dataModel, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, docId + " updateDocument success"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, docId + "updateDocument failure", e));
    }

    private DocumentReference getDocumentFromCollection(String docID, String collectionPath) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collectionPath)
                .document(docID);
    }

    private <T> LiveData<T> convertToLiveData(DocumentReference docRef, Class<T> valueType) {
        MutableLiveData<T> liveData = new MutableLiveData<>();

        docRef.addSnapshotListener((value, err) -> {
            if (err != null) {
                Log.d(DATA_DEBUG, "database snapshot error", err);
            } else if (value.exists()) {
                liveData.postValue(value.toObject(valueType));
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
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collection)
                .orderBy(field)
                .startAt(queryText)
                .endAt(queryText + "\uf8ff"); // StackOverflow hacks...
    }

    private Query queryArrayContains(String collection, String field, String queryText) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collection)
                .whereArrayContains(field, queryText);
    }

    private Task<Void> deleteDocument(String docID, String collectionPath) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        return UserProfile.builder()
                .withDisplayName(dataModel.getDisplayName())
                .withGender(dataModel.getGender())
                .withBirthday(LocalDate.parse(dataModel.getBirthday()))
                .withCountry(dataModel.getCountry())
                .withUid(dataModel.getUid())
                .withDisplayPicUri(Uri.parse(dataModel.getDisplayPicUri()))
                .withBio(Optional.fromNullable(dataModel.getBio()))
                .addAllPreferences(dataModel.getPreferences())
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
        GeoPoint geoPoint = dataModel.getLocation();
        Point location = Point.fromLngLat(geoPoint.getLongitude(), geoPoint.getLatitude());

        return Game.builder()
                .withGameName(dataModel.getGameName())
                .withSport(dataModel.getSport())
                .withLocation(location)
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
}
