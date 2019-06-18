package wjhj.orbital.sportsmatchfindingapp.repo;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;


import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.auth.UserState;

public class SportalDB implements ISportalDB {
    private static final String DATA_DEBUG = "SportalDB";

    @Override
    public void addUser(String userUid, User user) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(userUid)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Add user success"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "User add failed", e));
    }

    @Override
    public void updateUser(String userUid, User user) {
        update(userUid, "Users", user);
    }

    @Override
    public Task<User> getUser(String userUid) {
        return convertToObject(getDocumentFromCollection(userUid, "Users"), User.class);
    }

    @Override
    public Task<List<User>> selectUsers(String field, String queryText) {
        return queryCollection("Users", field, queryText)
                .continueWith(task -> task.getResult().toObjects(User.class));
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
                    addGameToUser(UserState.getUsername(), docRef.getId());

                    //Also add to the remaining participating users
                    for (String user : game.getUsernames()) {
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
    public Task<List<Game>> selectGames(String field, String queryText) {
        return queryCollection("Games", field, queryText)
                .continueWith(task -> task.getResult().toObjects(Game.class));
    }

    @Override
    public void deleteGame(String gameId) {
        delete(gameId, "Games");
    }

    //Helper methods
    private void addGameToUser(String username, String gameID) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(username)
                .update("pendingGames", FieldValue.arrayUnion(gameID))
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Added game to " + username))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game to " + username + " failed", e));
    }

    private void update(String docId, String collectionPath, Object obj) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath)
                .document(docId)
                .set(obj, SetOptions.merge())
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

    private Task<QuerySnapshot> queryCollection(String collection, String field, String queryText) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collection)
                .orderBy(field)
                .startAt(queryText)
                .endAt(queryText + "\uf8ff") // StackOverflow hacks...
                .get();
    }

    private void delete(String docID, String collectionPath) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath)
                .document(docID)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, docID + " successfully deleted!"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Error deleting document", e));
    }
}
