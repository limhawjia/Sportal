package wjhj.orbital.sportsmatchfindingapp.homepage.searchpage;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.InverseMethod;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DiffUtil;

import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java9.util.stream.StreamSupport;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.homepage.gamespage.GamesDiffCallback;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;


public class SearchViewModel extends ViewModel {
    private SportalRepo repo;

    private LiveData<String> sportsSelectionText;
    private MutableLiveData<ImmutableList<Sport>> sportsSelection;

    private LiveData<List<Game>> gamesData;

    private MutableLiveData<String> searchParameter;

    public SearchViewModel(ImmutableList<Sport> sportPreferences) {
        this.repo = SportalRepo.getInstance();

        this.sportsSelection = new MutableLiveData<>();
        this.sportsSelection.setValue(sportPreferences);
        this.sportsSelectionText = Transformations
                .map(sportsSelection, this::configureSportsSelectionText);
        this.searchParameter = new MutableLiveData<>();

        gamesData = Transformations.switchMap(sportsSelection, listOfSports -> {
            MediatorLiveData<List<Game>> data = new MediatorLiveData<>();
            HashMap<Sport, LiveData<List<Game>>> gamesGroupedBySport = new HashMap<>();

            data.addSource(searchParameter, para -> {

                Map<String, Game> allGames = new ConcurrentHashMap<>();
                for (Sport sport : listOfSports) {
                    //Todo: refactor this to return result of combined query of search parameter and sport type
                    LiveData<List<Game>> listOfGamesPerSport = repo
                            .selectGamesStartingWith("sport", sport.toString().toUpperCase());
                    data.addSource(listOfGamesPerSport, games -> {
                        if (games != null) {
                            Map<String, Game> newGamesMap = new HashMap<>();
                            StreamSupport.stream(games).forEach(game -> {
                                allGames.put(game.getUid(), game);
                                newGamesMap.put(game.getUid(), game);
                            });
                            StreamSupport.stream(allGames.values())
                                    .filter(game -> game.getSport() == sport)
                                    .forEach(game -> {
                                        if (!newGamesMap.containsKey(game.getUid())) {
                                            allGames.remove(game.getUid());
                                        }
                                    });
                        }
                        data.setValue(new ArrayList<Game>(allGames.values()));
                    });
                }
            });

            searchParameter.setValue("");
            return data;
        });


    }

    public LiveData<String> getSportsSelectionText() {
        return sportsSelectionText;
    }

    public MutableLiveData<String> getSearchParameter() {
        return searchParameter;
    }
    public void setSearchParameter(String parameter) {
        if (!parameter.equals(searchParameter.getValue())) {
            searchParameter.setValue(parameter);
        }
    }

    public LiveData<List<Game>> getGamesLiveData() {
        return gamesData;
    }

    private String configureSportsSelectionText(ImmutableList<Sport> sports) {
        if (sports.size() == 0) {
            return "None";
        } else if (sports.size() == 1) {
            return sports.toArray(new Sport[1])[0].toString();
        } else {
            return sports.size() + " sports";
        }
    }
}
