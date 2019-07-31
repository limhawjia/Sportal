package wjhj.orbital.sportsmatchfindingapp.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class UserProfileViewModel extends ViewModel {

    private SportalRepo repo;
    private LiveData<UserProfile> currUser;
    private LiveData<Map<GameStatus, List<String>>> gameIds;
    private LiveData<Map<GameStatus, List<Game>>> games;
    private LiveData<ImmutableList<Sport>> sportsPreferences;

    public UserProfileViewModel(String userUid) {
        repo = SportalRepo.getInstance();
        currUser = repo.getUser(userUid);
        gameIds = Transformations.map(currUser, UserProfile::getGames);
        games = loadAllGames();
        sportsPreferences = Transformations.map(currUser, UserProfile::getPreferences);
    }

    public LiveData<UserProfile> getCurrUser() {
        return currUser;
    }

    public LiveData<Map<GameStatus, List<String>>> getGameIds() {
        return gameIds;
    }

    public LiveData<Map<GameStatus, List<Game>>> getGames() {
        return games;
    }

    private LiveData<Map<GameStatus, List<Game>>> loadAllGames() {
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

    public LiveData<ImmutableList<Sport>> getSportsPreferences() {
        return sportsPreferences;
    }
}
