package wjhj.orbital.sportsmatchfindingapp.repo;

import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.function.Predicate;

public interface ISportalDB {

    void addUser(String userUid, User user);

    void updateUser(String userUid, User user);

    Task<User> getUser(String userUid);

    Task<List<User>> selectUsers(Predicate<User> pred);

    void deleteUser(String userUid);


    void addGame(String currUserUid, Game game);

    void updateGame(String gameId, Game game);

    Task<Game> getGame(String gameId);

    Task<List<Game>> selectGames(Predicate<Game> pred);

    void deleteGame(String gameId);
}
