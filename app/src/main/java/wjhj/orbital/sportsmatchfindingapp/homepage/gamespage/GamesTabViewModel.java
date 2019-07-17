package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class GamesTabViewModel extends ViewModel {

    private String GAMES_TAB_DEBUG = "games_tab";
    private MediatorLiveData<List<Game>> gamesLiveData = new MediatorLiveData<>();
    private MutableLiveData<List<Game>> filteredGames = new MutableLiveData<>();
    private MutableLiveData<Comparator<Game>> currentSort = new MutableLiveData<>();

    public GamesTabViewModel(LiveData<List<Game>> source) {
        currentSort.setValue((game1, game2) -> game1.getStartTime().compareTo(game2.getStartTime()));

        gamesLiveData.addSource(source, newGames -> {
            if (newGames == null) {
                newGames = new ArrayList<>();
            }
            Log.d(GAMES_TAB_DEBUG, "list of games changed!");
            Collections.sort(newGames, currentSort.getValue());
            gamesLiveData.setValue(newGames);
        });

        gamesLiveData.addSource(currentSort, newComparator -> {
            Log.d(GAMES_TAB_DEBUG, "list of games sorted!");
            if (gamesLiveData.getValue() != null) {
                List<Game> currGames = new ArrayList<>(gamesLiveData.getValue());
                Collections.sort(currGames, newComparator);
                gamesLiveData.setValue(currGames);
            }
        });
    }

    public void sortGames(Comparator<Game> comparator) {
        currentSort.setValue(comparator);
    }

    public void filterSports(Sport filter) {
        if (gamesLiveData.getValue() != null) {
            List<Game> filtered = new ArrayList<>();
            for (Game game : gamesLiveData.getValue()) {
                if (game.getSport() == filter) {
                    filtered.add(game);
                }
            }
            filteredGames.setValue(filtered);
        }
    }

    public void filterDifficulty(Difficulty filter) {
        if (gamesLiveData.getValue() != null) {
            List<Game> filtered = new ArrayList<>();
            for (Game game : gamesLiveData.getValue()) {
                if (game.getSkillLevel() == filter) {
                    filtered.add(game);
                }
            }
            filteredGames.setValue(filtered);
        }
    }

    public LiveData<List<Game>> getGamesLiveData() {
        return gamesLiveData;
    }

    public LiveData<List<Game>> getFilteredGames() {
        if (gamesLiveData.getValue() != null) {
            filteredGames.setValue(gamesLiveData.getValue());
        }
        return filteredGames;
    }
}
