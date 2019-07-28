package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;

import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class ChatPageViewModelFactory implements ViewModelProvider.Factory {

    private String currUserUid;
    private String channelUrl;
    private InputMethodManager iMM;

    ChatPageViewModelFactory(String currUserUid, String channelUrl, InputMethodManager iMM) {
        this.currUserUid = currUserUid;
        this.channelUrl = channelUrl;
        this.iMM = iMM;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (ChatPageViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(String.class, String.class, InputMethodManager.class)
                        .newInstance(currUserUid, channelUrl, iMM);

            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Cannot create instance of " + modelClass, e);
            }
        }

        throw new IllegalArgumentException("Unexpected model class " + modelClass);
    }
}
