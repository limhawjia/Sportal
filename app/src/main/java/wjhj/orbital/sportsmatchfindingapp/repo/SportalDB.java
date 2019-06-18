package wjhj.orbital.sportsmatchfindingapp.repo;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;
import java.util.function.Predicate;

import wjhj.orbital.sportsmatchfindingapp.auth.UserState;

public class SportalDB implements ISportalDB {
    private static final String DATA_DEBUG = "SportalDB";

    @Override
    public void addUser(String userUid, User user) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(userUid)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Add user complete"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "User add failed", e));
    }

    @Override
    public void updateUser(String userUid, User user) {

    }

    @Override
    public Task<User> getUser(String userUid) {
        return convertToObject(getDocumentFromCollection(userUid, "Users"), User.class);
    }

    @Override
    public Task<List<User>> selectUsers(Predicate<User> pred) {
        return null;
    }

    @Override
    public void deleteUser(String userUid) {

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

    }

    @Override
    public Task<Game> getGame(String gameID) {
        return convertToObject(getDocumentFromCollection(gameID, "Games"), Game.class);
    }

    @Override
    public Task<List<Game>> selectGames(Predicate<Game> pred) {
        return null;
    }

    @Override
    public void deleteGame(String gameId) {

    }

    private void addGameToUser(String username, String gameID) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(username)
                .update("pendingGames", FieldValue.arrayUnion(gameID))
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Added game to " + username))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game to " + username + " failed", e));
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
}
