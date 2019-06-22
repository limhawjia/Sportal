package wjhj.orbital.sportsmatchfindingapp.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    public static final String SIGNUP_DEBUG = "signup";

    private Authentications auths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auths = new Authentications(this);
    }

    private void OnSignUpClick(String displayName, String email, String password) {

    }
}
