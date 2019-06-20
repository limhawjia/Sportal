package wjhj.orbital.sportsmatchfindingapp.login;

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
import com.google.firebase.auth.FirebaseUser;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.LoginActivityBinding;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_DEBUG = "login";
    public static final int GOOGLE_SIGN_IN_RC = 1;

    private LoginViewModel loginViewModel;
    private LoginActivityBinding binding;

    private Authentications auths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOGIN_DEBUG, "Login activity created");
        super.onCreate(savedInstanceState);

        auths = new Authentications(this);

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
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(LOGIN_DEBUG, "sign in w email/password success");
                                updateOnLoggedIn(auths.getCurrentFirebaseUser());
                            } else {
                                Log.d(LOGIN_DEBUG, "sign in w email/password failure", task.getException());
                                Toast.makeText(this,
                                        "Authentication failed.", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });


        binding.googleLoginButton.setOnClickListener(view -> {
            Intent googleSignInIntent = auths.googleSignInClient.getSignInIntent();
            startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN_RC);
        });

        binding.signupButton.setOnClickListener(view -> {
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currUser = auths.getCurrentFirebaseUser();

        if (currUser != null) {
            updateOnLoggedIn(currUser);
        }

        GoogleSignInAccount googleAccount = auths.getLastSignedInGoogleAccount(this);

        if (googleAccount != null) {
            auths.firebaseAuthWithGoogle(googleAccount)
                    .addOnSuccessListener(this, result -> {
                        Log.d(LOGIN_DEBUG, "Sign in with Google credentials success");
                        updateOnLoggedIn(result.getUser());
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
            //Task returned here is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = signInTask.getResult(ApiException.class);

                auths.firebaseAuthWithGoogle(account)
                        .addOnSuccessListener(this, result -> {
                            Log.d(LOGIN_DEBUG, "Sign in with Google credentials success");
                            updateOnLoggedIn(result.getUser());
                        })
                        .addOnFailureListener(this, e -> {
                            Log.d(LOGIN_DEBUG, "Sign in with Google credentials failure", e);
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                    .show();
                        });
            } catch (ApiException e) {
                Log.d(LOGIN_DEBUG, "Google sign in fail", e);
            }
        }
    }


    private void updateOnLoggedIn(FirebaseUser currUser) {
        //navigate to homepage
    }
}
