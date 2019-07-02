package wjhj.orbital.sportsmatchfindingapp.repo;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public class SportalRepo implements ISportalRepo {
    private static final String DATA_DEBUG = "SportalRepo";

    @Override
    public void addUser(String uid, UserProfile userProfile) {
        Log.d(DATA_DEBUG, "hi");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserProfileDataModel dataModel = toUserProfileDataModel(userProfile);

        db.collection("Users")
                .document(uid)
                .set(dataModel)
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, uid + " added"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, uid + " add failed", e));
    }

    @Override
    public void updateUser(String uid, UserProfile userProfile) {
        UserProfileDataModel dataModel = toUserProfileDataModel(userProfile);
        updateDocument(uid, "Users", dataModel);
    }

    @Override
    public Task<UserProfile> getUser(String userUid) {
        Task<UserProfileDataModel> dataModelTask = convertToObject(
                getDocumentFromCollection(userUid, "Users"), UserProfileDataModel.class);

        return dataModelTask.continueWith(task -> toUserProfile(task.getResult()));
    }

    @Override
    public Task<List<UserProfile>> selectUsersStartingWith(String field, String queryText) {
        return queryStartingWith("Users", field, queryText)
                .continueWith(task ->
                        toUserProfiles(task.getResult().toObjects(UserProfileDataModel.class)));
    }

    @Override
    public Task<List<UserProfile>> selectUsersArrayContains(String field, String queryText) {
        return queryArrayContains("Users", field, queryText)
                .continueWith(task ->
                        toUserProfiles(task.getResult().toObjects(UserProfileDataModel.class)));
    }

    @Override
    public void deleteUser(String userUid) {
        deleteDocument(userUid, "Users");
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
    public void addGame(String gameUid, Game game) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        GameDataModel dataModel = toGameDataModel(game);

        db.collection("Games")
                .document(gameUid)
                .set(dataModel)
                .addOnSuccessListener(documentReference -> {
                    Log.d(DATA_DEBUG, "Add game complete.");
                    // If game added successfully, also add game to all users participating
                    for (String user : game.getParticipatingUids()) {
                        addGameToUser(user, gameUid);
                    }
                })
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game failed.", e));

    }

    @Override
    public void updateGame(String gameId, Game game) {
        GameDataModel dataModel = toGameDataModel(game);
        updateDocument(gameId, "Games", dataModel);
    }

    @Override
    public Task<Game> getGame(String gameID) {
        Task<GameDataModel> dataModelTask =
                convertToObject(getDocumentFromCollection(gameID, "Games"), GameDataModel.class);
        return dataModelTask.continueWith(task -> toGame(task.getResult()));
    }

    @Override
    public Task<List<Game>> selectGamesStartingWith(String field, String queryText) {
        return queryStartingWith("Games", field, queryText)
                .continueWith(task -> toGames(task.getResult().toObjects(GameDataModel.class)));
    }

    @Override
    public Task<List<Game>> selectGamesArrayContains(String field, String queryText) {
        return queryArrayContains("Games", field, queryText)
                .continueWith(task -> toGames(task.getResult().toObjects(GameDataModel.class)));
    }

    @Override
    public void deleteGame(String gameId) {
        deleteDocument(gameId, "Games");
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

    private void updateDocument(String docId, String collectionPath, Object dataModel) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath)
                .document(docId)
                .set(dataModel, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, docId + " updateDocument success"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, docId + "updateDocument failure", e));
    }

    private Task<DocumentSnapshot> getDocumentFromCollection(String docID, String collectionPath) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collectionPath)
                .document(docID)
                .get()
                .addOnSuccessListener(snapshot -> Log.d(DATA_DEBUG, "Document retrieval success"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Document retrieval failed", e));
    }

    private <T> Task<T> convertToObject (Task<DocumentSnapshot> snapshotTask, Class<T> valueType) {
        return snapshotTask.continueWith(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                return snapshot.toObject(valueType);
            } else {
                Log.d(DATA_DEBUG,  snapshot.toString() + " does not exist.");
                return null;
            }
        });
    }

    private Task<QuerySnapshot> queryStartingWith(String collection, String field, String queryText) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collection)
                .orderBy(field)
                .startAt(queryText)
                .endAt(queryText + "\uf8ff") // StackOverflow hacks...
                .get()
                .addOnSuccessListener(snapshot -> Log.d(DATA_DEBUG, snapshot + " retrieved"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "query collection failure", e));
    }
    
    private Task<QuerySnapshot> queryArrayContains(String collection, String field, String queryText) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collection)
                .whereArrayContains(field, queryText)
                .get()
                .addOnSuccessListener(snapshot -> Log.d(DATA_DEBUG, snapshot + " retrieved"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "query collection failure", e));
    }

    private void deleteDocument(String docID, String collectionPath) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath)
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
        for (String s : oldMap.keySet()) {
            List<String> games = oldMap.get(s);
            if (games != null) {
                newMap.put(GameStatus.fromString(s), games);
            }
        }

        return UserProfile.builder()
                .withDisplayName(dataModel.getDisplayName())
                .withGender(dataModel.getGender())
                .withBirthday(LocalDate.parse(dataModel.getBirthday()))
                .withUid(dataModel.getUid())
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
        Location location = new Location("");
        location.setLatitude(dataModel.getLocation().getLatitude());
        location.setLongitude(dataModel.getLocation().getLongitude());

        return Game.builder()
                .withGameName(dataModel.getGameName())
                .withDescription(dataModel.getDescription())
                .withSport(dataModel.getSport())
                .withLocation(location)
                .withMinPlayers(dataModel.getMinPlayers())
                .withMaxPlayers(dataModel.getMaxPlayers())
                .withSkillLevel(dataModel.getSkillLevel())
                .withStartTime(LocalDateTime.parse(dataModel.getStartTime()))
                .withEndTime(LocalDateTime.parse(dataModel.getEndTime()))
                .withUid(dataModel.getUid())
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
