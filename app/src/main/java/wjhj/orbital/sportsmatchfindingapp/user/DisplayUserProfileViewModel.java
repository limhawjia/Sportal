package wjhj.orbital.sportsmatchfindingapp.user;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class DisplayUserProfileViewModel extends ViewModel {

    private SportalRepo repo;
    private LiveData<UserProfile> userProfile;
    private String currUserUid;
    private boolean isCurrentUser;

    public DisplayUserProfileViewModel(String userUid) {
           repo = SportalRepo.getInstance();
           userProfile = repo.getUser(userUid);

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        currUserUid = currUser == null ? "" : currUser.getUid();
        isCurrentUser = (currUserUid.equals(userUid));
    }

    public LiveData<Uri> getDisplayPicUri() {
        return Transformations.map(userProfile, UserProfile::getDisplayPicUri);
    }

    public LiveData<String> getDisplayName() {
        return Transformations.map(userProfile, UserProfile::getDisplayName);
    }

    public LiveData<String> getBio() {
        return Transformations.map(userProfile, profile -> profile.getBio().or("No bio"));
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public LiveData<Boolean> isFriend() {
        return Transformations.map(userProfile, profile -> profile.getFriendUids().contains(currUserUid));
    }

}
