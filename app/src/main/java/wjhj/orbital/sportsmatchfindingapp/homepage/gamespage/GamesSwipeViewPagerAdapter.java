package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;


public class GamesSwipeViewPagerAdapter extends FragmentPagerAdapter {

    private Map<GameStatus, List<String>> allGameIds;

    public GamesSwipeViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        allGameIds = new EnumMap<>(GameStatus.class);
        for (GameStatus gameStatus : GameStatus.values()) {
            allGameIds.put(gameStatus, new ArrayList<>());
        }
    }

    public void updateGameIds(Map<GameStatus, List<String>> newAllGameIds) {
        if (newAllGameIds != null && !newAllGameIds.isEmpty()) {
            allGameIds = new EnumMap<>(newAllGameIds);
        }

        for (GameStatus gameStatus : GameStatus.values()) {
            if (!allGameIds.containsKey(gameStatus)) {
                allGameIds.put(gameStatus, new ArrayList<>());
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        GameStatus gameStatus = GameStatus.fromId(position);
        return GamesTabFragment
                .newInstance(gameStatus.toString(), new ArrayList<>(allGameIds.get(gameStatus)));
    }

    @Override
    public int getItemPosition(@NonNull Object item) {
        if (item instanceof GamesTabFragment) {
            GamesTabFragment gamesTabFragment = (GamesTabFragment) item;
            GameStatus gameStatus = GameStatus.fromString(gamesTabFragment.getTabName());
            gamesTabFragment.updateIds(allGameIds.get(gameStatus));
        }

        return super.getItemPosition(item);
    }

    @Override
    public int getCount() {
        return allGameIds.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return GameStatus.fromId(position).toString();
    }
}
