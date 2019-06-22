package wjhj.orbital.sportsmatchfindingapp.auth;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.SignupActivityBinding;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SignupActivity extends AppCompatActivity {

    public static final String SIGNUP_DEBUG = "signup";

    private Authentications auths;
    private SignupActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auths = new Authentications(this);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_activity);
        Uri displayPicUri = Uri.parse("DEFAULT"); // TODO

        binding.signupSubmit.setOnClickListener(view -> {
            SignUpAuth signUpAuth = new SignUpAuth(binding.emailField.getText().toString(),
                    binding.passwordField.getText().toString(),
                    binding.nameField.getText().toString(),
                    displayPicUri);
            signUp(signUpAuth);
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
                    .addOnFailureListener(e -> {
                        Log.d(SIGNUP_DEBUG, "Create user failed", e);
                        Toast.makeText(this, "Failed to create user.",
                                Toast.LENGTH_SHORT).show();
                    })
                    .continueWithTask(task -> auths.updateProfile(signUpAuth, task.getResult()))
                    .addOnSuccessListener(aVoid -> {
                        Log.d(SIGNUP_DEBUG, "User created successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(SIGNUP_DEBUG, "Adding display name/pic failed", e);
                        Toast.makeText(this, "Set display name/pic failed",
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }


    private Task<Uri> uploadDisplayImageAndGetUri(Bitmap displayImage, String uid) {
        ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
        displayImage.compress(Bitmap.CompressFormat.PNG, 100, imageByteStream);
        byte[] imageBytes = imageByteStream.toByteArray();

        StorageReference imageReference = FirebaseStorage.getInstance()
                .getReference()
                .child("display-images")
                .child(uid + ".png");

        UploadTask upload = imageReference.putBytes(imageBytes);
        Task<Uri> imageUri = upload.continueWithTask(
                (snapshot) -> imageReference.getDownloadUrl());
        return imageUri;
    }

}
