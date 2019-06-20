package wjhj.orbital.sportsmatchfindingapp.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password =  new MutableLiveData<>();
    private MutableLiveData<UserLogin> userLoginLiveData;


    LiveData<UserLogin> getUserLogin() {
        if (userLoginLiveData == null) {
            userLoginLiveData = new MutableLiveData<>();
        }
            return userLoginLiveData;
    }

    public void onEmailAndPasswordLogin() {
        UserLogin user = new UserLogin(email.getValue(), password.getValue());
        userLoginLiveData.setValue(user);
    }

}
