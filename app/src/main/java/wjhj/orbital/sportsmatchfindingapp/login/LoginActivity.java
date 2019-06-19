package wjhj.orbital.sportsmatchfindingapp.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

import javax.annotation.Nullable;

import wjhj.orbital.sportsmatchfindingapp.R;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_DEBUG = "login";
    private static final String EMAIL_INPUT_TAG = "emailInput";
    private static final String PASSWORD_INPUT_TAG = "passwordInput";

    private FirebaseAuth mAuth;
    private TextInputEditText mEmailField;
    private TextInputEditText mPasswordField;
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

        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);
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

        mSignupButton.setOnClickListener(view -> {
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currUser = mAuth.getCurrentUser();

        if (currUser != null) {
            updateOnLoggedIn(currUser);
        }
    }

    private boolean isEmailValid(@Nullable Editable email) {
        return email != null && EmailValidator.getInstance().isValid(email.toString());
    }

    private boolean isPasswordValid(@Nullable Editable password) {
        return password != null && password.length() >= 8;
    }

    private void trySignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(LOGIN_DEBUG, "sign in w email/password success");
                        updateOnLoggedIn(mAuth.getCurrentUser());
                    } else {
                        Log.d(LOGIN_DEBUG, "sign in w email/password failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void updateOnLoggedIn(FirebaseUser currUser) {
        //navigate to homepage
    }
}
