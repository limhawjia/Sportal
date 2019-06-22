package wjhj.orbital.sportsmatchfindingapp.auth;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
