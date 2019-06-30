package wjhj.orbital.sportsmatchfindingapp.user;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class UserProfileViewModel extends ViewModel {

    private SportalRepo repo;
    private MutableLiveData<UserProfile> currUser;
    private MutableLiveData<Map<GameStatus, List<String>>> gameIds;
    private LiveData<Map<GameStatus, List<Game>>> games;

    public UserProfileViewModel(String userUid) {
        repo = new SportalRepo();
        currUser = new MutableLiveData<>();
        gameIds = new MutableLiveData<>();
        repo.getUser(userUid)
                .addOnSuccessListener(userProfile -> {
                    currUser.postValue(userProfile);
                    gameIds.postValue(userProfile.getGames());
                });
    }

    public LiveData<UserProfile> getCurrUser() {
        return currUser;
    }

    public LiveData<Map<GameStatus, List<Game>>> getGames() {
        if (games == null) {
            games = setUpGamesMapping();
        }
        return games;
    }

    @NonNull
    private LiveData<Map<GameStatus, List<Game>>> setUpGamesMapping() {
        return Transformations.switchMap(gameIds, input -> {
            MutableLiveData<Map<GameStatus, List<Game>>> liveData = new MutableLiveData<>();
            Map<GameStatus, List<Game>> backingMap = new EnumMap<>(GameStatus.class);

            for (GameStatus gameStatus : GameStatus.values()) {
                backingMap.put(gameStatus, new ArrayList<>());
            }
            liveData.setValue(backingMap);

            for (Map.Entry<GameStatus, List<String>> entry : input.entrySet()) {
                List<String> ids = entry.getValue();
                List<Task<Game>> gameTaskList = new ArrayList<>();
                for (String id : ids) {
                    Task<Game> gameTask = repo.getGame(id)
                            .addOnSuccessListener(game -> backingMap.get(entry.getKey()).add(game))
                            .addOnFailureListener(e -> Log.d("gamesSwipeView", "fail", e));
                    gameTaskList.add(gameTask);
                }
                Tasks.whenAllComplete(gameTaskList)
                        .addOnSuccessListener(tasks -> {
                            liveData.postValue(backingMap);
                        });
            }

            return liveData;
        });
    }
}
