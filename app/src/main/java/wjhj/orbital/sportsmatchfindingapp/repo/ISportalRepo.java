package wjhj.orbital.sportsmatchfindingapp.repo;

import com.google.android.gms.tasks.Task;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public interface ISportalRepo {

    void addUser(String uid, UserProfile userProfile);

    void updateUser(String uid, UserProfile userProfile);

    Task<UserProfile> getUser(String userUid);

    Task<List<UserProfile>> selectUsersStartingWith(String field, String queryText);

    Task<List<UserProfile>> selectUsersArrayContains(String field, String queryText);

    void deleteUser(String userUid);

    void addGame(Game game);

    void updateGame(String gameId, Game game);

    Task<Game> getGame(String gameId);

    Task<List<Game>> selectGamesStartingWith(String field, String queryText);

    Task<List<Game>> selectGamesArrayContains(String field, String queryText);

    void deleteGame(String gameId);
}
