package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;

public class GamesTabViewModelFactory implements ViewModelProvider.Factory {

    private final LiveData<List<Game>> source;

    GamesTabViewModelFactory(GameStatus gameStatus, UserProfileViewModel userProfileViewModel) {
        source = Transformations.map(userProfileViewModel.getGames(),
                value -> value.get(gameStatus));
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (GamesTabViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(LiveData.class).newInstance(source);

            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Cannot create instance of " + modelClass, e);
            }
        }

        throw new IllegalArgumentException("Unexpected model class " + modelClass);
    }
}
