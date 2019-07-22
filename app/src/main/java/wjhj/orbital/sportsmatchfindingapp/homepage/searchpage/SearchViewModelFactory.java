package wjhj.orbital.sportsmatchfindingapp.homepage.searchpage;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.InvocationTargetException;

import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    private ImmutableList<Sport> sportsPreferences;

    public SearchViewModelFactory(ImmutableList<Sport> sportsPreferences) {
        this.sportsPreferences = sportsPreferences;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (SearchViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(ImmutableList.class).newInstance(sportsPreferences);

            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Cannot create instance of " + modelClass, e);
            }
        }

        throw new IllegalArgumentException("Unexpected model class " + modelClass);
    }
}
