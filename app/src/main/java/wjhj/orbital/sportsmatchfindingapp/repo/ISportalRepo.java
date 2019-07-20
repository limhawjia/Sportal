package wjhj.orbital.sportsmatchfindingapp.repo;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public interface ISportalRepo {

    Task<Void> addUser(String uid, UserProfile userProfile);

    Task<Void> updateUser(String uid, UserProfile userProfile);

    LiveData<UserProfile> getUser(String userUid);

    LiveData<List<UserProfile>> selectUsersStartingWith(String field, String queryText);

    LiveData<List<UserProfile>> selectUsersArrayContains(String field, String queryText);

    Task<Void> deleteUser(String userUid);

    String generateGameUid();

    Task<Void> addGame(String gameId, Game game);

    Task<Void> updateGame(String gameId, Game game);

    LiveData<Game> getGame(String gameId);

    LiveData<List<Game>> selectGamesStartingWith(String field, String queryText);

    LiveData<List<Game>> selectGamesArrayContains(String field, String queryText);

    Task<Void> deleteGame(String gameId);
}
