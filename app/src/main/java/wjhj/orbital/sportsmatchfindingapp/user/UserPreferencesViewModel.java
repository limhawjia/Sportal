package wjhj.orbital.sportsmatchfindingapp.user;

import android.util.Log;
import android.widget.RadioGroup;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class UserPreferencesViewModel extends ViewModel {
    List<Sport> sports = new ArrayList<>();
    MutableLiveData<Gender> gender = new MutableLiveData<>();
    MutableLiveData<String> birthday = new MutableLiveData<>();
    MutableLiveData<String> location = new MutableLiveData<>();


    public List<Sport> getSportPreferences() {
        return sports;
    }

    public void onGenderChanged(RadioGroup radioGroup, int id) {
        switch (id) {
            case R.id.male:
                gender.setValue(Gender.MALE);
                break;
            case R.id.female:
                gender.setValue(Gender.FEMALE);
                break;
        }
        Log.d(PreferencesActivity.PREFERENCES_DEBUG, this.gender.getValue().toString());
    }

    public void setBirthday(String date) {
        this.birthday.setValue(date);
    }

    public MutableLiveData<String> getBirthday() {
        return this.birthday;
    }
}
