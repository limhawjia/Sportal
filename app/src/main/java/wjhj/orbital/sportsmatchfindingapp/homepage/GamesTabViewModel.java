package wjhj.orbital.sportsmatchfindingapp.homepage;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class GamesTabViewModel extends ViewModel {

    private SportalRepo repo = new SportalRepo();
    private MutableLiveData<List<Game>> games = new MutableLiveData<>();

    public void loadGames(List<String> ids) {
        List<Game> gamesList = new ArrayList<>();
        for (String id : ids) {
            repo.getGame(id).addOnSuccessListener(game -> {
                gamesList.add(game);
                games.postValue(gamesList);
            });
        }
    }

    public LiveData<List<Game>> getGames() {
        return games;
    }
}
