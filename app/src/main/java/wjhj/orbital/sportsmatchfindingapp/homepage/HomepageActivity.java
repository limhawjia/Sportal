package wjhj.orbital.sportsmatchfindingapp.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.auth.Authentications;
import wjhj.orbital.sportsmatchfindingapp.auth.LoginActivity;
import wjhj.orbital.sportsmatchfindingapp.databinding.HomepageActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.game.AddGameActivity;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.homepage.gamespage.GamesSwipeViewFragment;
import wjhj.orbital.sportsmatchfindingapp.homepage.searchpage.SearchFragment;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.DisplayUserProfileFragment;
import wjhj.orbital.sportsmatchfindingapp.user.UserPreferencesActivity;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModelFactory;

public class HomepageActivity extends AppCompatActivity {

    public static final String CURR_USER_TAG = "current_user";
    public static final String DISPLAY_PROFILE_PIC_TAG = "display_profile_pic";
    public static final String HOMEPAGE_DEBUG = "homepage";
    private static final int ADD_GAME_RC = 1;

    private FirebaseUser currUser;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private UserProfileViewModel userProfileViewModel;
    private HomepageActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(HOMEPAGE_DEBUG, "homepage activity created");

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        checkLoggedIn();
        checkProfileSetup();

        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(currUser.getUid());
        userProfileViewModel = ViewModelProviders.of(this, factory)
                .get(UserProfileViewModel.class);


        binding = DataBindingUtil.setContentView(this, R.layout.homepage_activity);
        Toolbar toolbar = (Toolbar) binding.topToolbar;
        setSupportActionBar(toolbar);

        binding.bottomNav.setOnNavigationItemSelectedListener(navListener);
        ((Toolbar) binding.topToolbar).setOnMenuItemClickListener(menuListener);
        binding.homepageAddGameButton.setOnClickListener(view -> {
            Intent addGameIntent = new Intent(this, AddGameActivity.class);
            startActivityForResult(addGameIntent, ADD_GAME_RC);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLoggedIn();
    }

    private void checkLoggedIn() {
        if (currUser == null) {
            Toast.makeText(this, "Not logged in. Redirecting...", Toast.LENGTH_SHORT)
                    .show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    private void checkProfileSetup() {
        SportalRepo repo = SportalRepo.getInstance();
        repo.isProfileSetUp(currUser.getUid())
                .addOnSuccessListener(this, setup -> {
                    if (!setup) {
                        Toast.makeText(this, "Please setup user profile", Toast.LENGTH_SHORT)
                                .show();
                        Intent profileSetUpIntent = new Intent(this, UserPreferencesActivity.class);
                        startActivity(profileSetUpIntent);
                        finish();
                    }
                });
    }

    private void logOut() {
        Authentications auths = new Authentications();
        auths.logOutFirebase();
        auths.logOutGoogle(this);
        Intent logoutIntent = new Intent(this, LoginActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment fragment = new Fragment();// IMPLEMENT PROPERLY
        switch (item.getItemId()) {
            case R.id.nav_home:
                //todo
                break;
            case R.id.nav_games:
                fragment = GamesSwipeViewFragment.newInstance();
                break;
            case R.id.nav_search:
                ImmutableList.Builder<Sport> builder = new ImmutableList.Builder<>();
                LiveData<ImmutableList<Sport>> source = userProfileViewModel.getSportsPreferences();
                userProfileViewModel.getSportsPreferences()
                        .observe(this, new Observer<ImmutableList<Sport>>() {
                            @Override
                            public void onChanged(ImmutableList<Sport> sports) {
                                builder.addAll(sports);
                                source.removeObserver(this);
                            }
                        });
                fragment = SearchFragment.newInstance(builder.build());
                break;
            case R.id.nav_social:
                fragment = DisplayUserProfileFragment.newInstance("9EdldWC2b3ZW0z13UwGBPJ6a5Vv1");
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.homepage_fragment_container, fragment)
                .commit();

        return true;
    };

    private Toolbar.OnMenuItemClickListener menuListener = item -> {
        switch (item.getItemId()) {
            case R.id.options_profile:
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = manager.findFragmentByTag(DISPLAY_PROFILE_PIC_TAG);
                if (fragment == null) {
                    transaction.add(R.id.homepage_secondary_fragment_container,
                            DisplayUserProfileFragment.newInstance(currUser.getUid()),
                            DISPLAY_PROFILE_PIC_TAG)
                            .addToBackStack(null);
                } else {
                    transaction.replace(R.id.homepage_secondary_fragment_container,
                            fragment, DISPLAY_PROFILE_PIC_TAG);
                }
                transaction.commit();
                break;
            case R.id.options_logout:
                logOut();
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
