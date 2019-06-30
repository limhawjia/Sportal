package wjhj.orbital.sportsmatchfindingapp.user;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.auth.User;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class UserProfileViewModel extends ViewModel {

    private UserProfile currUser;
    private MutableLiveData<List<Game>> confirmedGames;

    public MutableLiveData<List<Game>> getConfirmedGames() {
        //TESTS
        List<Game> games = new ArrayList<>();
        Game game1 = Game.builder()
                .withGameName("SOCCER :D")
                .withDescription("TMR 6pm OR DIE")
                .withSport(Sport.BADMINTON)
                .withLocation(new Location(""))
                .withMinPlayers(4)
                .withMaxPlayers(6)
                .withSkillLevel(Difficulty.INTERMEDIATE)
                .withStartTime(LocalDateTime.now())
                .withEndTime(LocalDateTime.of(2019, 7, 1, 12, 12))
                .withUid("iamgame")
                .addParticipatingUids("123123", "1231", "531")
                .build();

        Game game2 = game1.withGameName("BLAA")
                .withDescription("Efef3ff");

        Game game3 = game2.withGameName("PEWWWW")
                .withEndTime(LocalDateTime.now());

        games.add(game1);
        games.add(game2);
        games.add(game3);
        games.add(game1);
        games.add(game2);
        games.add(game3);
        games.add(game1);
        games.add(game2);
        games.add(game3);

        MutableLiveData<List<Game>> liveData = new MutableLiveData<>();
        liveData.setValue(games);
        confirmedGames = liveData;

        return confirmedGames;
    }
}
