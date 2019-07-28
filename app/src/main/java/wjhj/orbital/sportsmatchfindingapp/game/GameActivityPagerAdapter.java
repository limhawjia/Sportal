package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;

import com.google.common.collect.ImmutableList;

import wjhj.orbital.sportsmatchfindingapp.homepage.searchpage.SearchFragment;

public class GameActivityPagerAdapter extends FragmentPagerAdapter {
    private String gameUid;

    GameActivityPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GameDetailsFragment.newInstance(gameUid);
            case 1:
                return GamesBoardFragment.newInstance(gameUid);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Details";
            case 1:
                return "Discussion";
            default:
                return "";
        }
    }

    public void setGame(String uid) {
        this.gameUid = uid;
    }
}
