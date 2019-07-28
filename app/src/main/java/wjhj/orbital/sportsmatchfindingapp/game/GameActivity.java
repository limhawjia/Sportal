package wjhj.orbital.sportsmatchfindingapp.game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.GameActionbarBinding;
import wjhj.orbital.sportsmatchfindingapp.databinding.GameActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public class GameActivity extends AppCompatActivity {
    public static String GAME_UID = "game_uid";
    private int ADD_GAME_RC = 2;
    private GameActivityBinding binding;
    private GameActivityPagerAdapter mPagerAdapter;
    private String gameUid;
    private LiveData<Game> mGameLiveData;

    public GameActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameUid = getIntent().getStringExtra(GAME_UID);
        mGameLiveData = SportalRepo.getInstance().getGame(gameUid);

        binding = DataBindingUtil.setContentView(this, R.layout.game_activity);
        setUpPageAdapter();
        setUpActionBar();

        setContentView(binding.getRoot());
    }

    private void setUpActionBar() {
        setSupportActionBar((Toolbar) binding.topToolbar);
        Transformations.map(mGameLiveData, Game::getGameName)
                .observe(this, getSupportActionBar()::setTitle);
    }

    private void setUpPageAdapter() {
        mPagerAdapter = new GameActivityPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setGame(gameUid);
        binding.gameActivityViewPager.setAdapter(mPagerAdapter);
        binding.gameActivityTabLayout.setupWithViewPager(binding.gameActivityViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mGameLiveData.getValue() != null) {
            String creatorUid = mGameLiveData.getValue().getCreatorUid();
            String currUserUid = FirebaseAuth.getInstance().getUid();
            if (creatorUid != null && creatorUid.equals(currUserUid)) {
                getMenuInflater().inflate(R.menu.game_owner_menu, menu);
            }
            return true;
        } else {
            mGameLiveData.observe(this, game -> {
                if (game != null) {
                    String creatorUid = mGameLiveData.getValue().getCreatorUid();
                    String currUserUid = FirebaseAuth.getInstance().getUid();
                    if (creatorUid != null && creatorUid.equals(currUserUid)) {
                        getMenuInflater().inflate(R.menu.game_owner_menu, menu);
                    }
                    invalidateOptionsMenu();
                }
            });
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent addGameIntent = new Intent(this, AddGameActivity.class);
            addGameIntent.putExtra(GAME_UID, gameUid);
            startActivityForResult(addGameIntent, ADD_GAME_RC);
        }
        return true;
    }

}
