package wjhj.orbital.sportsmatchfindingapp.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;

public class UserProfileViewModel extends ViewModel {

    private MutableLiveData<List<Game>> confirmedGames;

    public MutableLiveData<List<Game>> getConfirmedGames() {

        //TESTS
        List<Game> games = new ArrayList<>();
        Game game1 = Game.builder()
                .addName("SOCCER :D")
                .addDetails("TMR 6pm OR DIE")
                .build();

        Game game2 = Game.builder()
                .addDetails("Haw not invited")
                .addName("dota lan anyone?")
                .build();

        Game game3 = Game.builder()
                .addName("no details...")
                .build();

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
