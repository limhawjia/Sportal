package wjhj.orbital.sportsmatchfindingapp.auth;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import wjhj.orbital.sportsmatchfindingapp.R;

class Authentications {

    final FirebaseAuth firebaseAuth;
    final GoogleSignInClient googleSignInClient;

    Authentications(Context activityContext) {
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activityContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activityContext, gso);
    }

    FirebaseUser getCurrentFirebaseUser() {
        return firebaseAuth.getCurrentUser();
    }

    GoogleSignInAccount getLastSignedInGoogleAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }


    Task<AuthResult> trySignIn(LoginAuth loginAuth) {
        return firebaseAuth.signInWithEmailAndPassword(loginAuth.getEmail(), loginAuth.getPassword());
    }

    Task<AuthResult> tryCreateUser(SignUpAuth signUpAuth) {
        return firebaseAuth.createUserWithEmailAndPassword(signUpAuth.getEmail(), signUpAuth.getPassword());
    }

    Task<Void> updateProfile(SignUpAuth signUpAuth, AuthResult authResult) {
        UserProfileChangeRequest additionalInfo = new UserProfileChangeRequest.Builder()
                .setDisplayName(signUpAuth.getDisplayName())
                .setPhotoUri(signUpAuth.getDisplayPic())
                .build();
        return authResult.getUser().updateProfile(additionalInfo);
    }

    Task<AuthResult> firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return firebaseAuth.signInWithCredential(credential);

    }

}
