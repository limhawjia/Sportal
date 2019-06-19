package wjhj.orbital.sportsmatchfindingapp.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import wjhj.orbital.sportsmatchfindingapp.R;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_DEBUG = "login";
    private static final String USERNAME_INPUT = "usernameInput";
    private static final String PASSWORD_INPUT = "passwordInput";

    private FirebaseAuth mAuth;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private ImageButton mFacebookLogin;
    private ImageButton mGoogleLogin;
    private ImageButton mTwitterLogin;
    private TextView mSignupButton;
    private Button mLoginButton;

    private String mUsernameInput;
    private String mPasswordInput;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOGIN_DEBUG, "Login activity created");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();
        mUsernameField = findViewById(R.id.username_field);
        mPasswordField = findViewById(R.id.password_field);
        mFacebookLogin = findViewById(R.id.login_facebook);
        mGoogleLogin = findViewById(R.id.login_google);
        mTwitterLogin = findViewById(R.id.login_twitter);
        mSignupButton = findViewById(R.id.signup_button);
        mLoginButton = findViewById(R.id.login_button);

        if (savedInstanceState != null) {
            mUsernameField.setText(savedInstanceState.getString(USERNAME_INPUT));
            mPasswordField.setText(savedInstanceState.getString(PASSWORD_INPUT));
        }

        mUsernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mUsernameInput = s.toString();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPasswordInput = s.toString();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something...
            }
        });
        mGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something...
            }
        });
        mTwitterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something...
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something...
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something...
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currUser = mAuth.getCurrentUser();
        //WIP HELP
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(USERNAME_INPUT, mUsernameInput);
        savedInstanceState.putString(PASSWORD_INPUT, mPasswordInput);

        super.onSaveInstanceState(savedInstanceState);
    }
}
