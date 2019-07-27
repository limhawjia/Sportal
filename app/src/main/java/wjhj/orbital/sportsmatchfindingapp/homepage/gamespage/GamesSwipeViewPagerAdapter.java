package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;


public class GamesSwipeViewPagerAdapter extends FragmentPagerAdapter {

    GamesSwipeViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        GameStatus gameStatus = GameStatus.fromId(position);
        return GamesTabFragment
                .newInstance(gameStatus.toString());
    }

    @Override
    public int getCount() {
        return GameStatus.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return GameStatus.fromId(position).toString();
    }
}
