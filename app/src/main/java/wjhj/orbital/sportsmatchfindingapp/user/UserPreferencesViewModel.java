package wjhj.orbital.sportsmatchfindingapp.user;

import android.net.Uri;
import android.util.Log;
import android.widget.RadioGroup;

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
    private ValidationInput<Gender> gender;
    private MutableLiveData<Boolean> success;
    private MutableLiveData<List<Sport>> sportPreferences;

    private List<ValidationInput<?>> validationsList;

    public UserPreferencesViewModel() {
        displayPicUri = new MutableLiveData<>();
        bio = new MutableLiveData<>();
        birthday = new ValidationInput<>(date -> date != null && !date.isAfter(LocalDate.now()),
                "Please enter a valid birthday");
        country = new ValidationInput<>(country -> country != null, "Please select a country");
        gender = new ValidationInput<>(gender -> gender != null, "");
        success = new MutableLiveData<>();
        sportPreferences = new MutableLiveData<>();
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

    public ValidationInput<Gender> getGender() {
        return this.gender;
    }

    public LiveData<List<Sport>> getSportPreferences() {
        return sportPreferences;
    }

    private void setSportPreferences(List<Sport> sportPreferences) {
        this.sportPreferences.setValue(sportPreferences);
    }

    public void updateSportPreferences(boolean[] sportSelections) {
        List<Sport> sportsPreferences = sportPreferences.getValue();
        if (sportsPreferences == null) {
            sportsPreferences = new ArrayList<>();
        }

        for (int i = 0; i < sportSelections.length; i++) {
            Sport sport = Sport.values()[i];
            boolean selected = sportSelections[i];
            boolean contains = sportsPreferences.contains(sport);
            if (selected && !contains) {
                sportsPreferences.add(sport);
            } else if (!selected && contains) {
                sportsPreferences.remove(sport);
            }
        }

        setSportPreferences(sportsPreferences);
    }

    public boolean[] getSportSelections() {
        boolean[] selections = new boolean[Sport.values().length];
        List<Sport> currList = sportPreferences.getValue();
        if (currList != null) {
            for (Sport sport : currList) {
                selections[sport.ordinal()] = true;
            }
        }

        return selections;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    void updateProfile(String displayName, String currUserUid) {
        StreamSupport.stream(validationsList).forEach(ValidationInput::validate);

        if (StreamSupport.stream(validationsList)
                .allMatch(input -> input.getState() == ValidationInput.State.VALIDATED)) {

            List<Sport> preferences = sportPreferences.getValue() == null
                    ? new ArrayList<>()
                    : sportPreferences.getValue();

            UserProfile userProfile = UserProfile.builder()
                    .withDisplayName(displayName)
                    .withGender(gender.getInput())
                    .withBirthday(birthday.getInput())
                    .withCountry(country.getInput())
                    .withUid(currUserUid)
                    .withBio(Optional.fromNullable(bio.getValue()))
                    .addAllPreferences(preferences)
                    .build();

            Log.d("preferences", userProfile.toString());

            SportalRepo repo = SportalRepo.getInstance();
            repo.addUser(currUserUid, userProfile);

            Uri selectedUri = displayPicUri.getValue();
            if (selectedUri != null) {
                Authentications auths = new Authentications();
                auths.uploadDisplayImageAndGetUri(selectedUri, currUserUid)
                        .addOnSuccessListener(uri ->
                                repo.updateUser(currUserUid, userProfile.withDisplayPicUri(uri)))
                        .addOnFailureListener(e ->
                                Log.d("preferences", "profile pic upload failed", e));
            }

            success.setValue(true);
        }
    }

}
