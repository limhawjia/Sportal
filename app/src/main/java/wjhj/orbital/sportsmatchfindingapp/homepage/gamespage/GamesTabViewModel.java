package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class GamesTabViewModel extends ViewModel {

    private SportalRepo repo = new SportalRepo();
    private MutableLiveData<List<Game>> games = new MutableLiveData<>();

    public void loadGames(List<String> ids) {
        List<Task<Game>> gamesTasks = new ArrayList<>();
        List<Game> loadedGames = new ArrayList<>();

        for (String id : ids) {
            gamesTasks.add(repo.getGame(id).addOnSuccessListener(loadedGames::add));
        }

        Tasks.whenAllComplete(gamesTasks)
                .addOnSuccessListener(tasks -> games.postValue(loadedGames));
    }

    public void sortGames(Comparator<Game> comparator) {
        if (games.getValue() != null) {
            List<Game> currGames = new ArrayList<>(games.getValue());
            Collections.sort(currGames, comparator);
            games.setValue(currGames);
        }
    }

    public LiveData<List<Game>> getGames() {
        return games;
    }
}
