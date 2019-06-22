package wjhj.orbital.sportsmatchfindingapp.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseUser;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.SignupActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.user.PreferencesActivity;

public class SignupActivity extends AppCompatActivity {

    public static final String SIGNUP_DEBUG = "signup";
    public static final int PICK_DISPLAY_IMAGE_RC = 1;

    private Authentications auths;
    private SignupActivityBinding binding;
    private Uri displayPicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auths = new Authentications(this);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_activity);

        binding.signupSubmit.setOnClickListener(view -> {
            SignUpAuth signUpAuth = new SignUpAuth(binding.emailField.getText().toString(),
                    binding.passwordField.getText().toString(),
                    binding.nameField.getText().toString(),
                    displayPicUri);
            signUp(signUpAuth);
        });

        binding.addDisplayPicButton.setOnClickListener(view -> {
            Intent pickImageIntent = new Intent();
            pickImageIntent.setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(pickImageIntent, PICK_DISPLAY_IMAGE_RC);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DISPLAY_IMAGE_RC && resultCode == RESULT_OK) {
            if (data != null) {
                this.displayPicUri = data.getData();
                binding.signupProfilePic.setImageURI(this.displayPicUri);
            }
        }
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
                        updateOnSignedUp(result.getUser());
                    })
                    .addOnFailureListener(e -> {
                        Log.d(SIGNUP_DEBUG, "Create user failed", e);
                        Toast.makeText(this, "Failed to create user.",
                                Toast.LENGTH_SHORT).show();
                    })
                    .continueWithTask(task -> auths.updateProfile(signUpAuth, task.getResult()));

        }
    }

    private void updateOnSignedUp(FirebaseUser currUser) {
        Intent preferencesIntent = new Intent(this, PreferencesActivity.class);
        preferencesIntent.putExtra(LoginActivity.CURR_USER_TAG, currUser);
        startActivity(preferencesIntent);
    }
}
