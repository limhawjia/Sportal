package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;


public class GamesSwipeViewPagerAdapter extends FragmentPagerAdapter {

    private Map<GameStatus, List<Game>> allGames;

    public GamesSwipeViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        allGames = new EnumMap<>(GameStatus.class);
        for (GameStatus gameStatus : GameStatus.values()) {
            allGames.put(gameStatus, new ArrayList<>());
        }
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
        return allGames.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return GameStatus.fromId(position).toString();
    }
}
