package wjhj.orbital.sportsmatchfindingapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.SignupActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.user.PreferencesActivity;

public class SignUpActivity extends AppCompatActivity {

    public static final String SIGNUP_DEBUG = "signup";
    private static final String DISPLAY_NAME_TAG = "display_name;";

    private Authentications auths;
    private SignupActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auths = new Authentications();
        binding = DataBindingUtil.setContentView(this, R.layout.signup_activity);

        binding.signupSubmit.setOnClickListener(view -> {
            SignUpAuth signUpAuth = new SignUpAuth(binding.emailField.getText().toString(),
                    binding.passwordField.getText().toString(),
                    binding.nameField.getText().toString());
            updateOnSignedUp(signUpAuth);
       //     signUp(signUpAuth);
        });

    }

    private void signUp(SignUpAuth signUpAuth) {
        if (!signUpAuth.isEmailValid()) {
            binding.emailInput.setError("Not a valid email address");
        } else if (!signUpAuth.isPasswordValid()) {
            binding.passwordInput.setError("Password must be at least 8 characters!");
        } else if (!signUpAuth.isDisplayNameValid()) {
            binding.nameInput.setError("Please enter a name");
        } else {
            auths.tryCreateUser(signUpAuth)
                    .addOnSuccessListener(result -> {
                        Log.d(SIGNUP_DEBUG, "User created successfully");
                        updateOnSignedUp(signUpAuth);
                    })
                    .addOnFailureListener(e -> {
                        Log.d(SIGNUP_DEBUG, "Create user failed", e);
                        Toast.makeText(this, "Failed to create user.",
                                Toast.LENGTH_SHORT).show();
                    });

        }
    }

    private void updateOnSignedUp(SignUpAuth signUpAuth) {
        Intent preferencesIntent = new Intent(this, PreferencesActivity.class);
        preferencesIntent.putExtra(DISPLAY_NAME_TAG, signUpAuth.getDisplayName());
        startActivity(preferencesIntent);
        finish();
    }
}
