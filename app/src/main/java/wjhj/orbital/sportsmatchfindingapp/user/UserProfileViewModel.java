package wjhj.orbital.sportsmatchfindingapp.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class UserProfileViewModel extends ViewModel {

    private SportalRepo repo;
    private LiveData<UserProfile> currUser;
    private LiveData<Map<GameStatus, List<String>>> gameIds;
    private LiveData<Map<GameStatus, List<Game>>> games;

    public UserProfileViewModel(String userUid) {
        repo = new SportalRepo();
        currUser = new MutableLiveData<>();
        gameIds = new MutableLiveData<>();
        currUser = repo.getUser(userUid);
        gameIds = Transformations.map(currUser, UserProfile::getGames);
        games = new MutableLiveData<>();
    }

    public LiveData<UserProfile> getCurrUser() {
        return currUser;
    }

    public LiveData<Map<GameStatus, List<String>>> getGameIds() {
        return gameIds;
    }

    public LiveData<Map<GameStatus, List<Game>>> getGames() {
        return Transformations.switchMap(gameIds, newGameIds -> {
            MediatorLiveData<Map<GameStatus, List<Game>>> mapMediatorLiveData = new MediatorLiveData<>();
            Map<GameStatus, List<Game>> allGamesMap = new EnumMap<>(GameStatus.class);

            for (Map.Entry<GameStatus, List<String>> entry : newGameIds.entrySet()) {
                HashMap<String, Game> games = new HashMap<>();
                List<String> ids = entry.getValue();

                for (String id : ids) {
                    mapMediatorLiveData.addSource(repo.getGame(id), value -> {
                        games.put(value.getUid(), value);
                        allGamesMap.put(entry.getKey(), new ArrayList<>(games.values()));
                        mapMediatorLiveData.postValue(allGamesMap);
                    });
                }
            }
            return mapMediatorLiveData;
        });
    }
}
