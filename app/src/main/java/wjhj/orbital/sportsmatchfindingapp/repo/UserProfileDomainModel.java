package wjhj.orbital.sportsmatchfindingapp.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public enum UserProfileDomainModel {
    INSTANCE;

    public LiveData<UserProfile> userProfileLiveData;
    public LiveData<Game> gameLiveData;
    private MutableLiveData<String> gameIdLiveData;
    private ISportalRepo repo = new SportalRepo();

    public void withUserID(String userID) {

    }
}
