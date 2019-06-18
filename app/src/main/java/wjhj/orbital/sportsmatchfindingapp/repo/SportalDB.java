package wjhj.orbital.sportsmatchfindingapp.repo;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import wjhj.orbital.sportsmatchfindingapp.auth.UserState;

public class SportalDB {
    private static final String DATA_DEBUG = "SportalDB";

    public void addUser(String username, User user) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Admin")
                .document("Usernames")
                .update(username, true)
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Add user 1/2 complete"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "User add failed", e));

        db.collection("Users")
                .document(username)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Add user 2/2 complete"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "User add failed", e));
    }


    public void addGame(Game game) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Games")
                .add(game)
                .addOnSuccessListener(documentReference -> {
                    Log.d(DATA_DEBUG, "Add game complete.");
                    String gameID = documentReference.getId();

                    //If game added successfully, also add game to this user;
                    addGameToUser(UserState.getUsername(), gameID);

                    //Also add to the remaining participating users
                    for (String user : game.getUsernames()) {
                        addGameToUser(user, gameID);
                    }
                })
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game failed.", e));

    }

    private void addGameToUser(String username, String gameID) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(username)
                .update("pendingGames", FieldValue.arrayUnion(gameID))
                .addOnSuccessListener(aVoid -> Log.d(DATA_DEBUG, "Added game to " + username))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Add game to " + username + " failed", e));
    }

    public Task<User> getUser(String username) {
        return getDocumentFromCollection(username, "Users")
                .continueWith(task -> {
                    DocumentSnapshot snapshot = task.getResult();

                    if (snapshot.exists()) {
                        return snapshot.toObject(User.class);
                    } else {
                        Log.d(DATA_DEBUG, username + " does not exist.");
                        return null;
                    }
                });
    }

    public Task<Game> getGame(String gameID) {
        return getDocumentFromCollection(gameID, "Games")
                .continueWith(task -> {
                    DocumentSnapshot snapshot = task.getResult();

                    if (snapshot.exists()) {
                        return snapshot.toObject(Game.class);
                    } else {
                        Log.d(DATA_DEBUG, "game " + gameID + " does not exist.");
                        return null;
                    }
                });
    }

    private Task<DocumentSnapshot> getDocumentFromCollection(String docID, String collectionPath) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection(collectionPath)
                .document(docID)
                .get()
                .addOnSuccessListener(snapshot -> Log.d(DATA_DEBUG, "Document retrieval success"))
                .addOnFailureListener(e -> Log.d(DATA_DEBUG, "Document retrieval failed", e));
    }

}
