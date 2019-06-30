package wjhj.orbital.sportsmatchfindingapp.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class UserProfileViewModelFactory implements ViewModelProvider.Factory {

    private final String userUid;

    public UserProfileViewModelFactory(String userUid) {
        this.userUid = userUid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (UserProfileViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(String.class).newInstance(userUid);

            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Cannot create instance of " + modelClass, e);
            }
        }

        throw new IllegalArgumentException("Unexpected model class " + modelClass);
    }
}
