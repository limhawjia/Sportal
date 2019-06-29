package wjhj.orbital.sportsmatchfindingapp.repo;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public class SportalRepo implements ISportalRepo {
    private static final String DATA_DEBUG = "SportalRepo";

    public void test() {
        //:)
    }

    @Override
    public void addUser(UserProfile userProfile) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserProfileDataModel dataModel = toUserProfileDataModel(userProfile);

        db.collection("Users")
                .document(dataModel.getUid())
                .set(dataModel)
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, dataModel.getUid() + " added"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, dataModel.getUid() + " add failed", e));
    }

    @Override
    public void updateUser(UserProfile userProfile) {
        UserProfileDataModel dataModel = toUserProfileDataModel(userProfile);
        update(dataModel.getUid(), "Users", dataModel);
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
        delete(userUid, "Users");
    }

    @Override
    public void addGame(String currUserUid, Game game) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Games")
                .document();

        docRef.set(game)
                .addOnSuccessListener(documentReference -> {
                    Log.d(DATA_DEBUG, "Add game complete.");

                    //If game added successfully, also add game to this user;
                    addGameToUser(currUserUid, docRef.getId());

                    //Also add to the remaining participating users
                    for (String user : game.getParticipating()) {
                        addGameToUser(user, docRef.getId());
                    }
                })
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game failed.", e));

    }

    @Override
    public void updateGame(String gameId, Game game) {
        update(gameId, "Games", game);
    }

    @Override
    public Task<Game> getGame(String gameID) {
        return convertToObject(getDocumentFromCollection(gameID, "Games"), Game.class);
    }

    @Override
    public Task<List<Game>> selectGamesStartingWith(String field, String queryText) {
        return queryStartingWith("Games", field, queryText)
                .continueWith(task -> task.getResult().toObjects(Game.class));
    }

    @Override
    public Task<List<Game>> selectGamesArrayContains(String field, String queryText) {
        return queryArrayContains("Games", field, queryText)
                .continueWith(task -> task.getResult().toObjects(Game.class));
    }

    @Override
    public void deleteGame(String gameId) {
        delete(gameId, "Games");
    }

    //Helper methods
    private void addGameToUser(String userUid, String gameID) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(userUid)
                .update("pendingGames", FieldValue.arrayUnion(gameID))
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Added game to " + userUid))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game to " + userUid + " failed", e));
    }

    private void update(String docId, String collectionPath, Object dataModel) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath)
                .document(docId)
                .set(dataModel, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, docId + " update success"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, docId + "update failure", e));
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
                Log.d(DATA_DEBUG,  valueType.getSimpleName() + " does not exist.");
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

    private void delete(String docID, String collectionPath) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath)
                .document(docID)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, docID + " successfully deleted!"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Error deleting document", e));
    }

    private UserProfileDataModel toUserProfileDataModel(UserProfile userProfile) {
        return new UserProfileDataModel(userProfile);
    }

    private UserProfile toUserProfile(UserProfileDataModel dataModel) {
        Map<GameStatus, List<String>> newMap = new HashMap<>();
        Map<String, List<String>> oldMap = dataModel.getGames();
        for (String s : oldMap.keySet()) {
            List<String> games = oldMap.get(s);
            if (games != null) {
                newMap.put(GameStatus.fromId(s), games);
            }
        }

        return UserProfile.builder()
                .withUid(dataModel.getUid())
                .withDisplayName(dataModel.getDisplayName())
                .withGender(dataModel.getGender())
                .withBirthday(LocalDate.parse(dataModel.getBirthday()))
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
}
