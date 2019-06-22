package wjhj.orbital.sportsmatchfindingapp.user;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.PreferencesActivityBinding;

public class PreferencesActivity extends AppCompatActivity {
    public static String PREFERENCES_DEBUG = "preferences";
    private PreferencesActivityBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(PREFERENCES_DEBUG, "Preferences created");
        binding = DataBindingUtil.setContentView(this, R.layout.preferences_activity);
    }
}
