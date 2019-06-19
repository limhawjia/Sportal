package wjhj.orbital.sportsmatchfindingapp.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.apache.commons.validator.routines.EmailValidator;

import javax.annotation.Nullable;

import wjhj.orbital.sportsmatchfindingapp.R;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_DEBUG = "login";
    public static final int GOOGLE_SIGN_IN_RC = 1;

    private FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private TextInputEditText mEmailField;
    private TextInputEditText mPasswordField;
    private SignInButton mGoogleSignInButton;
    private ImageButton mFacebookLogin;
    private ImageButton mGoogleLogin;
    private ImageButton mTwitterLogin;
    private TextView mSignupButton;
    private MaterialButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOGIN_DEBUG, "Login activity created");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mEmailField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);
        mGoogleSignInButton = findViewById(R.id.google_login_button);
        mFacebookLogin = findViewById(R.id.login_facebook);
        mGoogleLogin = findViewById(R.id.login_google);
        mTwitterLogin = findViewById(R.id.login_twitter);
        mSignupButton = findViewById(R.id.signup_button);
        mLoginButton = findViewById(R.id.login_button);


        mEmailField.setOnKeyListener((view, i, keyEvent) -> {
            if (isEmailValid(mEmailField.getText())) {
                mEmailField.setError(null);
            }
            return false;
        });

        mPasswordField.setOnKeyListener((view, i, keyEvent) -> {
            if (isPasswordValid(mPasswordField.getText())) {
                mPasswordField.setError(null);
            }
            return false;
        });

        mGoogleSignInButton.setOnClickListener(view -> {
            Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN_RC);
        });

        mLoginButton.setOnClickListener(view -> {
            if (!isPasswordValid(mPasswordField.getText())) {
                mPasswordField.setError("Password must be at least 8 characters!");
            } else if (!isEmailValid(mEmailField.getText())) {
                mEmailField.setError("Not a valid email address");
            } else {
                mPasswordField.setError(null);
                trySignIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currUser = mFirebaseAuth.getCurrentUser();

        if (currUser != null) {
            updateOnLoggedIn(currUser);
        }

        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (googleAccount != null) {
            //update w google
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_RC) {
            //Task returned here is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = signInTask.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.d(LOGIN_DEBUG, "Google sign in fail", e);
            }
        }
    }

    private boolean isEmailValid(@Nullable Editable email) {
        return email != null && EmailValidator.getInstance().isValid(email.toString());
    }

    private boolean isPasswordValid(@Nullable Editable password) {
        return password != null && password.length() >= 8;
    }

    private void trySignIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(LOGIN_DEBUG, "sign in w email/password success");
                        updateOnLoggedIn(mFirebaseAuth.getCurrentUser());
                    } else {
                        Log.d(LOGIN_DEBUG, "sign in w email/password failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(LOGIN_DEBUG, "firebaseAuth with Google: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, result -> {
                    Log.d(LOGIN_DEBUG, "Sign in with Google credentials success");
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    updateOnLoggedIn(user);
                })
                .addOnFailureListener(this, e -> {
                    Log.d(LOGIN_DEBUG, "Sign in with Google credentials failure", e);
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show();
                });
    }

    private void updateOnLoggedIn(FirebaseUser currUser) {
        //navigate to homepage
    }
}
