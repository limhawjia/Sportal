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
import wjhj.orbital.sportsmatchfindingapp.repo.GameSearchFilter;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;


public class SearchViewModel extends ViewModel {
    private SportalRepo repo;

    private LiveData<String> sportsSelectionText;
    private MutableLiveData<ImmutableList<Sport>> sportsSelection;

    private LiveData<List<Game>> liveGamesData;

    private MutableLiveData<String> searchParameter;
    private MediatorLiveData<GameSearchFilter> searchFilters;

    public SearchViewModel(ImmutableList<Sport> sportPreferences) {
        repo = SportalRepo.getInstance();

        searchFilters = new MediatorLiveData<>();
        searchFilters.setValue(GameSearchFilter.get());
        liveGamesData = Transformations.map(
                Transformations.switchMap(searchFilters, repo::getGamesWithFilters),
                map -> new ArrayList<>(map.values()));

        sportsSelection = new MutableLiveData<>();
        searchParameter = new MutableLiveData<>();

        sportsSelectionText = Transformations
                .map(sportsSelection, this::configureSportsSelectionText);

        searchFilters.addSource(sportsSelection, sports -> {
            Log.d("hi", "Sports selection changed: " + sports.size());
            GameSearchFilter filter = searchFilters.getValue();
            filter.setSportQuery(sports);
            searchFilters.setValue(filter);
        });
        searchFilters.addSource(searchParameter, para -> {
            Log.d("hi", "Search parameter changed to " + para);
            GameSearchFilter filter = searchFilters.getValue();
            filter.setNameQuery(para);
            searchFilters.setValue(filter);
        });

        sportsSelection.setValue(sportPreferences);
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

    public LiveData<List<Game>> getGamesData() {
        return liveGamesData;
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
