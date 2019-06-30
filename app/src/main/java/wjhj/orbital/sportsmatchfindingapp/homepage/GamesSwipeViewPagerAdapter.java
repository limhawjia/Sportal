package wjhj.orbital.sportsmatchfindingapp.homepage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class GamesSwipeViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> tabNames;

    public GamesSwipeViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<String> tabNames) {
        super(fm, behavior);
        this.tabNames = tabNames;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return GamesTabFragment.newInstance(tabNames.get(position));
    }

    @Override
    public int getCount() {
        return tabNames.size();
    }
}
