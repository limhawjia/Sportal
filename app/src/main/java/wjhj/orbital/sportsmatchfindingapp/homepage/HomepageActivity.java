package wjhj.orbital.sportsmatchfindingapp.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.auth.LoginActivity;
import wjhj.orbital.sportsmatchfindingapp.databinding.HomepageActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModelFactory;

public class HomepageActivity extends AppCompatActivity {

    public static final String CURR_USER_TAG = "current_user";
    public static final String HOMEPAGE_DEBUG = "homepage";

    private FirebaseUser currUser;
    private UserProfileViewModel userProfileViewModel;
    private HomepageActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(HOMEPAGE_DEBUG, "homepage activity created");

        initViewModel();

        binding = DataBindingUtil.setContentView(this, R.layout.homepage_activity);
        setSupportActionBar((Toolbar) binding.topToolbar);

        binding.bottomNav.setOnNavigationItemSelectedListener(navListener);
        ((Toolbar) binding.topToolbar).setOnMenuItemClickListener(menuListener);

    }

    private void initViewModel() {
        currUser = getIntent().getParcelableExtra(CURR_USER_TAG);
        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(currUser.getUid());
        userProfileViewModel = ViewModelProviders.of(this, factory)
                .get(UserProfileViewModel.class);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment fragment = new Fragment();// IMPLEMENT PROPERLY
        switch (item.getItemId()) {
            case R.id.nav_home:
                //todo
                break;
            case R.id.nav_games:
                ArrayList<String> gameStatuses = new ArrayList<>();
                for (GameStatus gameStatus : userProfileViewModel.getCurrUser().getGames().keySet()) {
                    gameStatuses.add(gameStatus.toString());
                }
                fragment = GamesSwipeViewFragment.newInstance(gameStatuses);
                break;
            case R.id.nav_search:
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

    private Toolbar.OnMenuItemClickListener menuListener = item -> {
        switch (item.getItemId()) {
            case R.id.options_profile:
                //todo
                break;
            case R.id.options_logout:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
                break;
        }

        return true;
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_options_menu, menu);
        return true;
    }
}
