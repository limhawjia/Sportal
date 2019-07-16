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

    public void updateGames(Map<GameStatus, List<Game>> newAllGames) {
        if (newAllGames != null && !newAllGames.isEmpty()) {
            allGames = new EnumMap<>(newAllGames);
        }

        for (GameStatus gameStatus : GameStatus.values()) {
            if (!allGames.containsKey(gameStatus)) {
                allGames.put(gameStatus, new ArrayList<>());
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        GameStatus gameStatus = GameStatus.fromId(position);
        return GamesTabFragment
                .newInstance(gameStatus.toString(), new ArrayList<>(allGames.get(gameStatus)));
    }

    @Override
    public int getItemPosition(@NonNull Object item) {
        if (item instanceof GamesTabFragment) {
            GamesTabFragment gamesTabFragment = (GamesTabFragment) item;
            GameStatus gameStatus = GameStatus.fromString(gamesTabFragment.getTabName());
            gamesTabFragment.updateGames(allGames.get(gameStatus));
        }

        return super.getItemPosition(item);
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
