package wjhj.orbital.sportsmatchfindingapp.dialogs;

import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import java9.util.stream.Stream;
import java9.util.stream.StreamSupport;
import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.game.TimeOfDay;
import wjhj.orbital.sportsmatchfindingapp.repo.GameSearchFilter;

public class SearchFilterDialogViewModel extends ViewModel {
    MutableLiveData<GameSearchFilter>  filter = new MutableLiveData<>();

    public SearchFilterDialogViewModel(GameSearchFilter searchFilter) {
        filter.setValue(searchFilter);
    }

    public MutableLiveData<GameSearchFilter> getFilter() {
        return filter;
    }

    public void toggleMorning(View view) {
        GameSearchFilter newFilter = filter.getValue();
        if (newFilter.getTimeOfDayQuery().contains(TimeOfDay.MORNING)) {
            newFilter.getTimeOfDayQuery().remove(TimeOfDay.MORNING);
        } else {
            newFilter.getTimeOfDayQuery().add(TimeOfDay.MORNING);
        }
        filter.setValue(newFilter);
    }

    public void toggleAfternoon(View view) {
        GameSearchFilter newFilter = filter.getValue();
        if (newFilter.getTimeOfDayQuery().contains(TimeOfDay.AFTERNOON)) {
            newFilter.getTimeOfDayQuery().remove(TimeOfDay.AFTERNOON);
        } else {
            newFilter.getTimeOfDayQuery().add(TimeOfDay.AFTERNOON);
        }
        filter.setValue(newFilter);
    }

    public void toggleNight(View view) {
        GameSearchFilter newFilter = filter.getValue();
        if (newFilter.getTimeOfDayQuery().contains(TimeOfDay.NIGHT)) {
            newFilter.getTimeOfDayQuery().remove(TimeOfDay.NIGHT);
        } else {
            newFilter.getTimeOfDayQuery().add(TimeOfDay.NIGHT);
        }
        filter.setValue(newFilter);
    }

    public void toggleBeginner(View view) {
        GameSearchFilter newFilter = filter.getValue();
        if (newFilter.getSkillLevelQuery().contains(Difficulty.BEGINNER)) {
            newFilter.getSkillLevelQuery().remove(Difficulty.BEGINNER);
        } else {
            newFilter.getSkillLevelQuery().add(Difficulty.BEGINNER);
        }
        filter.setValue(newFilter);
    }

    public void toggleIntermediate(View view) {
        GameSearchFilter newFilter = filter.getValue();
        if (newFilter.getSkillLevelQuery().contains(Difficulty.INTERMEDIATE)) {
            newFilter.getSkillLevelQuery().remove(Difficulty.INTERMEDIATE);
        } else {
            newFilter.getSkillLevelQuery().add(Difficulty.INTERMEDIATE);
        }
        filter.setValue(newFilter);
    }

    public void toggleAdvanced(View view) {
        GameSearchFilter newFilter = filter.getValue();
        if (newFilter.getSkillLevelQuery().contains(Difficulty.ADVANCED)) {
            newFilter.getSkillLevelQuery().remove(Difficulty.ADVANCED);
        } else {
            newFilter.getSkillLevelQuery().add(Difficulty.ADVANCED);
        }
        filter.setValue(newFilter);
    }

    public GameSearchFilter getFilters() {
        return filter.getValue();
    }
}
