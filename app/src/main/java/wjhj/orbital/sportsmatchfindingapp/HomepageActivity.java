package wjhj.orbital.sportsmatchfindingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

public class HomepageActivity extends AppCompatActivity {

    public static final String CURR_USER_TAG = "current_user";
    public static final String HOMEPAGE_DEBUG = "homepage";

    FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(HOMEPAGE_DEBUG, "homepage activity created");

        currUser = getIntent().getParcelableExtra(CURR_USER_TAG);
        setContentView(R.layout.activity_homepage);

        if (currUser != null) {
            Log.d(HOMEPAGE_DEBUG, "works!");
        }
    }
}
