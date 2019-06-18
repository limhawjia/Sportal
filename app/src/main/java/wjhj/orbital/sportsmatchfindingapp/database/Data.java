package wjhj.orbital.sportsmatchfindingapp.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import wjhj.orbital.sportsmatchfindingapp.auth.UserState;

public class Data {
    public static final String DATA_DEBUG = "data";

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addUser(String username, User user) {
        final String _username = username;
        Task<Void> future = db.collection("Admin").document("Usernames").update(username, true);
        future.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(DATA_DEBUG, "Add user 1/2 complete");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(DATA_DEBUG, "User add failed.");
            }
        });

        Task<Void> future1 = db.collection("Users").document(username).set(user);
        future1.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(DATA_DEBUG, "Add user 2/2 complete");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(DATA_DEBUG, "User add failed.");
            }
        });
    }

    public static void addGame(Game game) {
        final Game _game = game;

        Task<DocumentReference> future = db.collection("Games").add(game);
        future.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(DATA_DEBUG, "Add game complete.");

                //If game added successfully, also add game to this user;
                Task<Void> future1 = db.collection("Users").document(UserState.getUsername()).update("pendingGames", FieldValue.arrayUnion(documentReference.getId()));
                future1.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(DATA_DEBUG, "Added game to " + UserState.getUsername());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DATA_DEBUG, "Add game to " + UserState.getUsername() + " failed");
                    }
                });

                //Also add to the remaining participating users
                for (String user : _game.getUsernames()) {
                    Task<Void> future2 = db.collection("Users").document(UserState.getUsername()).update("pendingGames", FieldValue.arrayUnion(documentReference.getId()));
                    future2.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DATA_DEBUG, "Added game to " + UserState.getUsername());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(DATA_DEBUG, "Add game to " + UserState.getUsername() + " failed");
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(DATA_DEBUG, "Add game failed.");
            }
        });

    }

    public User getUser(String username) {
        Task<DocumentSnapshot> future = db.collection("Users").document(username).get();
        future.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(DATA_DEBUG, "User retrieval success.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(DATA_DEBUG, "User retrieval failed.");
            }
        });
        DocumentSnapshot user = future.getResult();
        if (!user.exists()) {
            Log.d(DATA_DEBUG, username + " does not exist.");
            return null;
        } else {
            return user.toObject(User.class);
        }
    }

    public Game getGame(String gameId) {
        Task<DocumentSnapshot> future = db.collection("Games").document(gameId).get();
        future.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(DATA_DEBUG, "Game retrieval success.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(DATA_DEBUG, "Game retrieval failed.");
            }
        });
        DocumentSnapshot game = future.getResult();
        if (!game.exists()) {
            Log.d(DATA_DEBUG, gameId + " does not exist.");
            return null;
        } else {
            return game.toObject(Game.class);
        }
    }
}
