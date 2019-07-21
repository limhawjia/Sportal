package wjhj.orbital.sportsmatchfindingapp.user;

import android.net.Uri;
import android.util.Log;
import android.widget.RadioGroup;

import androidx.databinding.Observable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.common.base.Optional;

import org.threeten.bp.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java9.util.stream.StreamSupport;
import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.auth.Authentications;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.maps.Country;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.utils.ValidationInput;

public class UserPreferencesViewModel extends ViewModel {

    private MutableLiveData<Uri> displayPicUri;
    private MutableLiveData<String> bio;
    private ValidationInput<LocalDate> birthday;
    private ValidationInput<Country> country;
    private ObservableInt countrySelection;
    private ValidationInput<Gender> gender;
    private MutableLiveData<Boolean> success;
    private List<Sport> sports;

    private List<ValidationInput<?>> validationsList;

    public UserPreferencesViewModel() {
        displayPicUri = new MutableLiveData<>();
        bio = new MutableLiveData<>();
        birthday = new ValidationInput<>(date -> date != null && !date.isAfter(LocalDate.now()),
                "Please enter a valid birthday");
        country = new ValidationInput<>(country -> country != null, "Please select a country");
        countrySelection = new ObservableInt(-1);
        gender = new ValidationInput<>(gender -> gender != null, "");
        success = new MutableLiveData<>();
        sports = new ArrayList<>();

        countrySelection.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId >= 0) {
                    country.setInput(Country.values()[propertyId]);
                }
            }
        });

        validationsList = new ArrayList<>();
        validationsList.add(birthday);
        validationsList.add(country);
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

    public ValidationInput<Country> getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country.setInput(country);
    }

    public ObservableInt getCountrySelection() {
        return countrySelection;
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

    public void updatePreferences(String displayName, String currUserUid) {
        StreamSupport.stream(validationsList).forEach(ValidationInput::validate);

        if (StreamSupport.stream(validationsList)
                .allMatch(input -> input.getState() == ValidationInput.State.VALIDATED)) {

            UserProfile userProfile = UserProfile.builder()
                    .withDisplayName(displayName)
                    .withGender(gender.getInput())
                    .withBirthday(birthday.getInput())
                    .withCountry(Country.AFGHANISTAN)
                    .withUid(currUserUid)
                    .withBio(Optional.fromNullable(bio.getValue()))
                    .addAllPreferences(sports)
                    .build();

            Log.d("preferences", userProfile.toString());
//
//            SportalRepo repo = SportalRepo.getInstance();
//            repo.addUser(currUserUid, userProfile);
//
//            Uri selectedUri = displayPicUri.getValue();
//            if (selectedUri != null) {
//                Authentications auths = new Authentications();
//                auths.uploadDisplayImageAndGetUri(selectedUri, currUserUid)
//                        .addOnSuccessListener(uri ->
//                                repo.updateUser(currUserUid, userProfile.withDisplayPicUri(uri)))
//                        .addOnFailureListener(e ->
//                                Log.d("preferences", "profile pic upload failed", e));
//            }

            success.setValue(true);
        }
    }

}
