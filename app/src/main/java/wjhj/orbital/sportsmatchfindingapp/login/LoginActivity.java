package wjhj.orbital.sportsmatchfindingapp.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import wjhj.orbital.sportsmatchfindingapp.R;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_DEBUG = "login";
    private static final String USERNAME_INPUT = "usernameInput";
    private static final String PASSWORD_INPUT = "passwordInput";

    EditText mUsernameField;
    EditText mPasswordField;
    ImageButton mFacebookLogin;
    ImageButton mGoogleLogin;
    ImageButton mInstagramLogin;
    ImageButton mTwitterLogin;
    TextView mSignupButton;
    Button mLoginButton;

    String mUsernameInput;
    String mPasswordInput;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOGIN_DEBUG, "Login activity created");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        mUsernameField = findViewById(R.id.username_field);
        mPasswordField = findViewById(R.id.password_field);
        mFacebookLogin = findViewById(R.id.login_facebook);
        mGoogleLogin = findViewById(R.id.login_google);
        mInstagramLogin = findViewById(R.id.login_instagram);
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
        mInstagramLogin.setOnClickListener(new View.OnClickListener() {
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(USERNAME_INPUT, mUsernameInput);
        savedInstanceState.putString(PASSWORD_INPUT, mPasswordInput);

        super.onSaveInstanceState(savedInstanceState);
    }
}
