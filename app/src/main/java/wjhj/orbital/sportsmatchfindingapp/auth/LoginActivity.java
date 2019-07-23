package wjhj.orbital.sportsmatchfindingapp.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import wjhj.orbital.sportsmatchfindingapp.homepage.HomepageActivity;
import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.LoginActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.user.UserPreferencesActivity;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_DEBUG = "login";
    public static final String GOOGLE_NAME_TAG = "google_name";
    public static final int GOOGLE_SIGN_IN_RC = 1;

    private LoginViewModel loginViewModel;
    private LoginActivityBinding binding;

    private Authentications auths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOGIN_DEBUG, "Login activity created");

        auths = new Authentications();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        binding.setLoginViewModel(loginViewModel);
        binding.setLifecycleOwner(this);

        loginViewModel.getUserLogin().observe(this, loginUser -> {
            if (!loginUser.isEmailValid()) {
                binding.emailField.setError("Not a valid email address");
            } else if (!loginUser.isPasswordValid()) {
                binding.passwordField.setError("Password must be at least 8 characters!");
            } else {
                auths.trySignIn(loginUser)
                        .addOnSuccessListener(this, task -> {
                            Log.d(LOGIN_DEBUG, "sign in w email/password success");
                            updateOnLoggedIn(task);
                        })
                        .addOnFailureListener(this, e -> {
                            Log.d(LOGIN_DEBUG, "sign in w email/password failure", e);
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(this, "Account does not exist", Toast.LENGTH_LONG)
                                        .show();
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
        });


        binding.googleLoginButton.setOnClickListener(view -> {
            Intent googleSignInIntent = auths.getGoogleSignInClient(this).getSignInIntent();
            startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN_RC);
        });

        binding.signupButton.setOnClickListener(view -> {
            Intent signupIntent = new Intent(this, SignUpActivity.class);
            startActivity(signupIntent);
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currUser = auths.getCurrentFirebaseUser();

        if (currUser != null) {
            Intent intent = new Intent(this, HomepageActivity.class);
            startActivity(intent);
            finish();
        }

        GoogleSignInAccount googleAccount = auths.getLastSignedInGoogleAccount(this);

        if (googleAccount != null) {
            auths.firebaseAuthWithGoogle(googleAccount)
                    .addOnSuccessListener(this, result -> {
                        Log.d(LOGIN_DEBUG, "Sign in with Google credentials success");
                        updateOnLoggedIn(result);
                    })
                    .addOnFailureListener(this, e -> {
                        Log.d(LOGIN_DEBUG, "Sign in with Google credentials failure", e);
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show();
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_RC) {
            if (resultCode == RESULT_OK) {
                //Task returned here is always completed, no need to attach a listener.
                Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = signInTask.getResult(ApiException.class);

                    auths.firebaseAuthWithGoogle(account)
                            .addOnSuccessListener(this, result -> {
                                Log.d(LOGIN_DEBUG, "Sign in with Google credentials success");
                                updateOnLoggedIn(result);
                            })
                            .addOnFailureListener(this, e -> {
                                Log.d(LOGIN_DEBUG, "Sign in with Google credentials failure", e);
                                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_LONG)
                                        .show();
                            });
                } catch (ApiException e) {
                    Log.d(LOGIN_DEBUG, "Google sign in fail", e);
                }
            } else {
                Log.d(LOGIN_DEBUG, "Google login cancelled with result code: " + resultCode);
            }
        }
    }

    private void updateOnLoggedIn(AuthResult authResult) {
        AdditionalUserInfo additionalUserInfo = authResult.getAdditionalUserInfo();

        if (additionalUserInfo != null && additionalUserInfo.isNewUser()) {
            if (additionalUserInfo.getProviderId().equals("google.com")) {
                Intent profileSetupIntent = new Intent(this, UserPreferencesActivity.class);
                try {
                    profileSetupIntent.putExtra(UserPreferencesActivity.DISPLAY_NAME_TAG,
                            (String) additionalUserInfo.getProfile().get("name"));
                } catch (ClassCastException e) {
                    Log.d(LOGIN_DEBUG, "Class cast exception", e);
                }
                startActivity(profileSetupIntent);
                finish();
            }
            return;
        }

        Intent homepageIntent = new Intent(this, HomepageActivity.class);
        startActivity(homepageIntent);
        finish();
    }
}
