package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class GameUidViewModelFactory implements ViewModelProvider.Factory {
    private String gameUid;

    GameUidViewModelFactory(String gameUid) {
        this.gameUid = gameUid;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (GameDetailsViewModel.class.isAssignableFrom(modelClass) ||
                GamesBoardViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(String.class).newInstance(gameUid);

            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Cannot create instance of " + modelClass, e);
            }
        }

        throw new IllegalArgumentException("Unexpected model class " + modelClass);
    }
}
