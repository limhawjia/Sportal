package wjhj.orbital.sportsmatchfindingapp.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;


import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.auth.LoginActivity;
import wjhj.orbital.sportsmatchfindingapp.databinding.HomepageActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.game.AddGameActivity;
import wjhj.orbital.sportsmatchfindingapp.homepage.gamespage.GamesSwipeViewFragment;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModelFactory;

public class HomepageActivity extends AppCompatActivity {

    public static final String CURR_USER_TAG = "current_user";
    public static final String HOMEPAGE_DEBUG = "homepage";
    private static final int ADD_GAME_RC = 1;

    private FirebaseUser currUser;
    private UserProfileViewModel userProfileViewModel;
    private HomepageActivityBinding binding;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(HOMEPAGE_DEBUG, "homepage activity created");
        currUser = getIntent().getParcelableExtra(CURR_USER_TAG);

        UserProfileViewModelFactory factory = new UserProfileViewModelFactory(currUser.getUid());
        userProfileViewModel = ViewModelProviders.of(this, factory)
                .get(UserProfileViewModel.class);


        binding = DataBindingUtil.setContentView(this, R.layout.homepage_activity);
        setSupportActionBar((Toolbar) binding.topToolbar);


        binding.bottomNav.setOnNavigationItemSelectedListener(navListener);
        ((Toolbar) binding.topToolbar).setOnMenuItemClickListener(menuListener);
        binding.homepageAddGameButton.setOnClickListener(view -> {
            Intent addGameIntent = new Intent(this, AddGameActivity.class);
            startActivityForResult(addGameIntent, ADD_GAME_RC);
        });

        Mapbox.getInstance(this,  getString(R.string.mapbox_access_token));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mapFragment = SupportMapFragment.newInstance();
        transaction.replace(R.id.homepage_fragment_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(mapboxMap -> {
            mapboxMap.setStyle(Style.DARK, style -> {
                Log.d(HOMEPAGE_DEBUG, "style loaded");
            });
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment fragment = new Fragment();// IMPLEMENT PROPERLY
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = mapFragment;
                break;
            case R.id.nav_games:
                fragment = GamesSwipeViewFragment.newInstance();
                break;
            case R.id.nav_search:
                Intent intent = new Intent(this, GameSearchActivity.class);
                intent.putExtra(HomepageActivity.CURR_USER_TAG, currUser);
                startActivity(intent);
                break;
            case R.id.nav_social:
                //todo
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
