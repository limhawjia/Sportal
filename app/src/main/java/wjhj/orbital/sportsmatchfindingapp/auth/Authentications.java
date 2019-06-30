package wjhj.orbital.sportsmatchfindingapp.auth;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.ExecutionException;

import wjhj.orbital.sportsmatchfindingapp.R;

public class Authentications {
    private static String AUTHENTICATION_DEBUG = "authentications";

    final FirebaseAuth firebaseAuth;

    public Authentications() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    FirebaseUser getCurrentFirebaseUser() {
        return firebaseAuth.getCurrentUser();
    }

    GoogleSignInAccount getLastSignedInGoogleAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    GoogleSignInClient getGoogleSignInClient(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(context, gso);
    }

    Task<AuthResult> trySignIn(LoginAuth loginAuth) {
        return firebaseAuth.signInWithEmailAndPassword(loginAuth.getEmail(), loginAuth.getPassword());
    }

    Task<AuthResult> tryCreateUser(SignUpAuth signUpAuth) {
        return firebaseAuth.createUserWithEmailAndPassword(signUpAuth.getEmail(), signUpAuth.getPassword());
    }

    Task<Void> updateProfile(SignUpAuth signUpAuth, AuthResult authResult) {
        UserProfileChangeRequest.Builder additionalInfo = new UserProfileChangeRequest.Builder()
                .setDisplayName(signUpAuth.getDisplayName());

        try {
            Uri uri = Tasks.await(uploadDisplayImageAndGetUri(signUpAuth.getDisplayPic(),
                    authResult.getUser().getUid()));
            additionalInfo = additionalInfo.setPhotoUri(uri);
        } catch (InterruptedException | ExecutionException e) {
            Log.d(AUTHENTICATION_DEBUG, "Upload image failed", e);
        }

        return authResult.getUser().updateProfile(additionalInfo.build());
    }

    private Task<Uri> uploadDisplayImageAndGetUri(Uri displayImage, String uid) {
        StorageReference imageReference = FirebaseStorage.getInstance()
                .getReference()
                .child("display-images")
                .child(uid + ".png");

        UploadTask upload = imageReference.putFile(displayImage);
        return upload.continueWithTask(snapshot -> imageReference.getDownloadUrl());
    }

    Task<AuthResult> firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return firebaseAuth.signInWithCredential(credential);
    }

}