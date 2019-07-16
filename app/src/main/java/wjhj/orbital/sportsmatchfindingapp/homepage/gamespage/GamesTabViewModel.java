package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;

public class GamesTabViewModel extends ViewModel {

    private MediatorLiveData<List<Game>> gamesLiveData = new MediatorLiveData<>();
    private MutableLiveData<List<Game>> filteredGames = new MutableLiveData<>();

    public GamesTabViewModel(GameStatus gameStatus, UserProfileViewModel userProfileViewModel) {
        gamesLiveData.addSource(userProfileViewModel.getGames(), new Observer<Map<GameStatus, List<Game>>>() {
            List<Game> prevGames;
            @Override
            public void onChanged(Map<GameStatus, List<Game>> gamesMap) {
                List<Game> newGames = gamesMap.get(gameStatus);
                if (prevGames == null || !prevGames.equals(newGames)) {
                    gamesLiveData.setValue(newGames);
                }
                prevGames = newGames;
            }
        });
    }

    public void loadGames(List<Game> games) {
        this.gamesLiveData.setValue(games);
//        List<LiveData<Game>> gamesLiveData = new ArrayList<>();
//        List<Game> loadedGames = new ArrayList<>();
//
//        for (String id : ids) {
//            gamesLiveData.add(repo.getGame(id));
//        }
//
//        Tasks.whenAllComplete(gamesLiveData)
//                .addOnSuccessListener(tasks -> {
//                    Collections.sort(loadedGames,
//                            (game1, game2) -> game1.getStartTime().compareTo(game2.getStartTime()));
//                    gamesLiveData.postValue(loadedGames);
//                });
    }

    public void sortGames(Comparator<Game> comparator) {
        if (gamesLiveData.getValue() != null) {
            List<Game> currGames = new ArrayList<>(gamesLiveData.getValue());
            Collections.sort(currGames, comparator);
            gamesLiveData.setValue(currGames);
        }
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
