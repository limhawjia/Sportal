package wjhj.orbital.sportsmatchfindingapp.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.HomepageActivityBinding;

public class HomepageActivity extends AppCompatActivity {

    public static final String CURR_USER_TAG = "current_user";
    public static final String HOMEPAGE_DEBUG = "homepage";

    private FirebaseUser currUser;
    private HomepageActivityBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(HOMEPAGE_DEBUG, "homepage activity created");

        currUser = getIntent().getParcelableExtra(CURR_USER_TAG);
        binding = DataBindingUtil.setContentView(this, R.layout.homepage_activity);

        binding.bottomNav
                .setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment fragment = new Fragment();// IMPLEMENT PROPERLY
        switch (item.getItemId()) {
            case R.id.nav_home:
                //todo
                break;
            case R.id.nav_games:
                //todo
                break;
            case R.id.nav_search:
                //todo
                break;
            case R.id.nav_explore:
                //todo
                break;
            case R.id.nav_social:
                //todo
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        return true;
    };

}
