package wjhj.orbital.sportsmatchfindingapp.repo;

import com.google.android.gms.tasks.Task;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public interface ISportalDB {

    void addUser(String userUid, UserProfile userProfile);

    void updateUser(String userUid, UserProfile userProfile);

    Task<UserProfile> getUser(String userUid);

    Task<List<UserProfile>> selectUsers(String field, String queryText);

    void deleteUser(String userUid);

    void addGame(String currUserUid, Game game);

    void updateGame(String gameId, Game game);

    Task<Game> getGame(String gameId);

    Task<List<Game>> selectGames(String field, String queryText);

    void deleteGame(String gameId);
}
