package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.common.collect.ImmutableList;

import wjhj.orbital.sportsmatchfindingapp.homepage.searchpage.SearchFragment;

public class GameActivityPagerAdapter extends FragmentPagerAdapter {
    private Game mGame;

    public GameActivityPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchFragment.newInstance(ImmutableList.<Sport>builder().build());
            case 1:
                return SearchFragment.newInstance(ImmutableList.<Sport>builder().build());
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

    public void setGame(Game game) {
        this.mGame = game;
    }
}
