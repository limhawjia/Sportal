package wjhj.orbital.sportsmatchfindingapp.homepage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class GamesSwipeViewPagerAdapter extends FragmentPagerAdapter {

    private String[] tabNames;

    public GamesSwipeViewPagerAdapter(@NonNull FragmentManager fm, int behavior, String[] tabNames) {
        super(fm, behavior);
        this.tabNames = tabNames;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return GamesTabFragment.newInstance(tabNames[position]);
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }
}
