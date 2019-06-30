package wjhj.orbital.sportsmatchfindingapp.user;

import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.auth.User;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class UserPreferencesViewModel extends ViewModel {
    List<Sport> sports = new ArrayList<>();
    MutableLiveData<Gender> gender = new MutableLiveData<>();
    MutableLiveData<String> birthday = new MutableLiveData<>();
    public MutableLiveData<String> displayName = new MutableLiveData<>();


    public List<Sport> getSportPreferences() {
        return sports;
    }

    public List<Sport> getSportPreferencesToUpdate() {
        sports.remove(0);
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

    public LocalDate getBirthday() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(birthday.getValue(), formatter);
    }

    public LiveData<String> getBirthdayLiveData() {
        return this.birthday;
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String s) {
        this.displayName.setValue(s);
    }

    public Gender getGender() {
        return this.gender.getValue();
    }
}
