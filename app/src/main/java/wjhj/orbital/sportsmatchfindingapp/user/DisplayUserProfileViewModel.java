package wjhj.orbital.sportsmatchfindingapp.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class DisplayUserProfileViewModel extends ViewModel {

    private SportalRepo repo;
    private LiveData<UserProfile> userProfile;
    private boolean isCurrentUser;

    public DisplayUserProfileViewModel(String userUid) {
           repo = SportalRepo.getInstance();
           userProfile = repo.getUser(userUid);

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String currUserUid = currUser == null ? "" : currUser.getUid();
        isCurrentUser = (currUserUid.equals(userUid));


    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }
}
