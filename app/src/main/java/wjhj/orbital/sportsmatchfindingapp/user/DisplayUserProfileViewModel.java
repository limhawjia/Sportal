package wjhj.orbital.sportsmatchfindingapp.user;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.maps.Country;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class DisplayUserProfileViewModel extends ViewModel {

    private LiveData<UserProfile> userProfile;
    private String currUserUid;
    private boolean isCurrentUser;
    private LiveData<Uri> displayPicUri;
    private LiveData<String> displayName;
    private LiveData<String> bio;
    private LiveData<Gender> gender;
    private LiveData<Country> country;
    private LiveData<List<Sport>> preferences;
    private LiveData<Boolean> isFriend;

    public DisplayUserProfileViewModel(String userUid) {
        SportalRepo repo = SportalRepo.getInstance();
        userProfile = repo.getUser(userUid);

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        currUserUid = currUser == null ? "" : currUser.getUid();
        isCurrentUser = (currUserUid.equals(userUid));

        displayPicUri = Transformations.map(userProfile, UserProfile::getDisplayPicUri);
        displayName = Transformations.map(userProfile, UserProfile::getDisplayName);
        bio = Transformations.map(userProfile, profile -> profile.getBio().or("No bio"));
        gender = Transformations.map(userProfile, UserProfile::getGender);
        country = Transformations.map(userProfile, UserProfile::getCountry);
        preferences = Transformations.map(userProfile, UserProfile::getPreferences);
        isFriend = Transformations.map(userProfile, profile -> profile.getFriendUids().contains(currUserUid));
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public LiveData<Uri> getDisplayPicUri() {
        return displayPicUri;
    }

    public LiveData<String> getDisplayName() {
        return displayName;
    }

    public LiveData<String> getBio() {
        return bio;
    }

    public LiveData<Gender> getGender() {
        return gender;
    }

    public LiveData<Country> getCountry() {
        return country;
    }

    LiveData<List<Sport>> getPreferences() {
        return preferences;
    }


    public LiveData<Boolean> isFriend() {
        return isFriend;
    }

}
