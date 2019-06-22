package wjhj.orbital.sportsmatchfindingapp.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password =  new MutableLiveData<>();
    private MutableLiveData<LoginAuth> userLoginLiveData;


    LiveData<LoginAuth> getUserLogin() {
        if (userLoginLiveData == null) {
            userLoginLiveData = new MutableLiveData<>();
        }
            return userLoginLiveData;
    }

    public void onEmailAndPasswordLogin() {
        LoginAuth user = new LoginAuth(email.getValue(), password.getValue());
        userLoginLiveData.setValue(user);
    }

}
