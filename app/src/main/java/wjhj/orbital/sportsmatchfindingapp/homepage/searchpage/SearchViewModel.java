package wjhj.orbital.sportsmatchfindingapp.homepage.searchpage;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.ImmutableList;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.repo.GameSearchFilter;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;


public class SearchViewModel extends ViewModel {
    private SportalRepo repo;

    private LiveData<String> sportsSelectionText;
    private MutableLiveData<ImmutableList<Sport>> sportsSelection;

    private MediatorLiveData<List<Game>> liveGamesData;

    private MutableLiveData<String> searchParameter;
    private MediatorLiveData<GameSearchFilter> searchFilters;
    private MutableLiveData<Comparator<Game>> sortComparator;

    public SearchViewModel(ImmutableList<Sport> sportPreferences) {
        repo = SportalRepo.getInstance();

        searchFilters = new MediatorLiveData<>();
        searchFilters.setValue(new GameSearchFilter());
        liveGamesData = new MediatorLiveData<>();
        LiveData<List<Game>> source1 = Transformations.map(Transformations.map(
                Transformations.switchMap(searchFilters, repo::getGamesWithFilters),
                map -> new ArrayList<>(map.values())), list -> {
            Collections.sort(list, sortComparator.getValue());
            return list;
        });
        liveGamesData.addSource(source1, liveGamesData::setValue);

        sortComparator = new MutableLiveData<>();
        sortComparator.setValue((game1, game2) -> Integer
                .compare(game1.getSport().ordinal(), game2.getSport().ordinal()));

        liveGamesData.addSource(sortComparator, newComparator -> {
            if (liveGamesData.getValue() != null) {
                List<Game> currGames = new ArrayList<>(liveGamesData.getValue());
                Collections.sort(currGames, newComparator);
                liveGamesData.setValue(currGames);
            }
        });

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

    public LiveData<GameSearchFilter> getSearchFilters() {
        return searchFilters;
    }

    public void postNewFilters(GameSearchFilter filters) {
        searchFilters.setValue(filters);
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
            return sports.size() + " Sports";
        }
    }

    public void sortGames(Comparator<Game> comparator) {
        sortComparator.setValue(comparator);
    }

    public void updateSports(List<Sport> sports) {
        sportsSelection.setValue(new ImmutableList.Builder<Sport>().addAll(sports).build());
    }
}
