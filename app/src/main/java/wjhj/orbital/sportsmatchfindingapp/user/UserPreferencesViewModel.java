package wjhj.orbital.sportsmatchfindingapp.user;

import android.net.Uri;
import android.widget.RadioGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import org.threeten.bp.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java9.util.stream.StreamSupport;
import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.utils.ValidationInput;

public class UserPreferencesViewModel extends ViewModel {

    private MutableLiveData<Uri> displayPicUri;
    private MutableLiveData<String> bio;
    private ValidationInput<LocalDate> birthday;
    private ValidationInput<Gender> gender;
    private MutableLiveData<Boolean> success;
    private List<Sport> sports;

    private List<ValidationInput<?>> validationsList;

    public UserPreferencesViewModel() {
        displayPicUri = new MutableLiveData<>();
        bio = new MutableLiveData<>();
        birthday = new ValidationInput<>(date -> date != null && !date.isAfter(LocalDate.now()),
                "Please enter a valid birthday");
        gender = new ValidationInput<>(gender -> gender != null, "");
        success = new MutableLiveData<>();
        sports = new ArrayList<>();

        validationsList = new ArrayList<>();
        validationsList.add(birthday);
        validationsList.add(gender);
    }


    public LiveData<Uri> getDisplayPicUri() {
        return displayPicUri;
    }

    public void setDisplayPicUri(Uri displayPicUri) {
        this.displayPicUri.setValue(displayPicUri);
    }

    public MutableLiveData<String> getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio.setValue(bio);
    }

    public void onGenderChanged(RadioGroup radioGroup, int id) {
        switch (id) {
            case R.id.male:
                gender.setInput(Gender.MALE);
                break;
            case R.id.female:
                gender.setInput(Gender.FEMALE);
                break;
        }
    }

    public ValidationInput<LocalDate> getBirthday() {
        return this.birthday;
    }


    public void setBirthday(LocalDate date) {
        this.birthday.setInput(date);
    }


    public ValidationInput<Gender> getGender() {
        return this.gender;
    }


    public List<Sport> getSportPreferences() {
        return sports;
    }

    public List<Sport> getSportPreferencesToUpdate() {
        sports.remove(0);
        return sports;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    public void updatePreferences(String currUserUid) {
        StreamSupport.stream(validationsList).forEach(ValidationInput::validate);

        if (StreamSupport.stream(validationsList)
                .allMatch(input -> input.getState() == ValidationInput.State.VALIDATED)) {
            success.setValue(true);
        }
    }


}
