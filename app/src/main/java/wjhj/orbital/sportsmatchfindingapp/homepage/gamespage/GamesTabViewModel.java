package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class GamesTabViewModel extends ViewModel {

    private MediatorLiveData<List<Game>> gamesLiveData = new MediatorLiveData<>();
    private MutableLiveData<List<Game>> filteredGames = new MutableLiveData<>();
    private MutableLiveData<Comparator<Game>> currentSort = new MutableLiveData<>();

    public GamesTabViewModel(LiveData<List<Game>> source) {
        currentSort.setValue((game1, game2) -> game1.getStartDateTime().compareTo(game2.getStartDateTime()));

        gamesLiveData.addSource(source, newGames -> {
            if (newGames == null) {
                newGames = new ArrayList<>();
            }
            Timber.d("list of games changed!");
            Collections.sort(newGames, currentSort.getValue());
            gamesLiveData.setValue(newGames);
        });

        gamesLiveData.addSource(currentSort, newComparator -> {
            Timber.d("list of games sorted!");
            if (gamesLiveData.getValue() != null) {
                List<Game> currGames = new ArrayList<>(gamesLiveData.getValue());
                Collections.sort(currGames, newComparator);
                gamesLiveData.setValue(currGames);
            }
        });
    }

    void sortGames(Comparator<Game> comparator) {
        currentSort.setValue(comparator);
    }

    void filterSports(Sport filter) {
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

    void filterDifficulty(Difficulty filter) {
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

    LiveData<List<Game>> getGamesLiveData() {
        return gamesLiveData;
    }

    LiveData<List<Game>> getFilteredGames() {
        if (gamesLiveData.getValue() != null) {
            filteredGames.setValue(gamesLiveData.getValue());
        }
        return filteredGames;
    }
}
