package wjhj.orbital.sportsmatchfindingapp.dialogs;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

import wjhj.orbital.sportsmatchfindingapp.homepage.gamespage.GamesTabViewModel;
import wjhj.orbital.sportsmatchfindingapp.repo.GameSearchFilter;

public class SearchFilterDialogViewModelFactory implements ViewModelProvider.Factory {
    GameSearchFilter filter;

    SearchFilterDialogViewModelFactory(GameSearchFilter filter) {
        this.filter = filter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (SearchFilterDialogViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(GameSearchFilter.class).newInstance(filter);

            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Cannot create instance of " + modelClass, e);
            }
        }

        throw new IllegalArgumentException("Unexpected model class " + modelClass);
    }
}
