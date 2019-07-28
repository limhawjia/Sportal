package wjhj.orbital.sportsmatchfindingapp.user;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelParams;
import com.sendbird.android.SendBirdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.maps.Country;
import wjhj.orbital.sportsmatchfindingapp.messaging.SendBirdConstants;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.utils.BatchTransformations;
import wjhj.orbital.sportsmatchfindingapp.utils.Result;

public class DisplayUserProfileViewModel extends ViewModel {

    private final SportalRepo repo;
    private final String mDisplayedUserUid;

    private String currUserUid;
    private boolean isCurrentUser;
    private LiveData<Boolean> isFriend;

    private LiveData<Map<GameStatus, List<String>>> allGameIds;
    private LiveData<List<Game>> pastGames;
    private LiveData<List<Game>> upcomingGames;
    private LiveData<Integer> numGamesPlayed;
    private LiveData<List<String>> allFriendUids;
    private LiveData<List<UserProfile>> allFriends;
    private LiveData<Integer> numFriends;
    private LiveData<List<String>> receivedFriendRequests;
    private LiveData<List<String>> sentFriendRequests;
    private LiveData<Boolean> isReceivedFriendRequest;
    private LiveData<Boolean> isSentFriendRequest;

    private LiveData<Uri> displayPicUri;
    private LiveData<String> displayName;
    private LiveData<String> bio;
    private LiveData<Gender> gender;
    private LiveData<Country> country;
    private LiveData<List<Sport>> preferences;

    private MutableLiveData<Result<GroupChannel>> sendMessageResult;


    public DisplayUserProfileViewModel(String displayedUserUid) {
        mDisplayedUserUid = displayedUserUid;
        repo = SportalRepo.getInstance();
        LiveData<UserProfile> userProfile = repo.getUser(displayedUserUid);

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        currUserUid = currUser == null ? "" : currUser.getUid();
        isCurrentUser = (currUserUid.equals(displayedUserUid));

        setUpGameTransformations(userProfile);
        setUpFriendTransformations(userProfile);
        setUpAttributeTransformations(userProfile);

        sendMessageResult = new MutableLiveData<>();
    }

    private void setUpGameTransformations(LiveData<UserProfile> userProfile) {
        allGameIds = Transformations.map(userProfile, UserProfile::getGames);
        numGamesPlayed = Transformations.map(allGameIds, games -> games.get(GameStatus.COMPLETED).size());
    }

    private void setUpFriendTransformations(LiveData<UserProfile> userProfile) {
        allFriendUids = Transformations.map(userProfile, UserProfile::getFriendUids);
        allFriends = loadFriends();
        numFriends = Transformations.map(allFriendUids, List::size);
        receivedFriendRequests = Transformations.map(userProfile, UserProfile::getReceivedFriendRequests);
        sentFriendRequests = Transformations.map(userProfile, UserProfile::getSentFriendRequests);
        isReceivedFriendRequest = Transformations.map(sentFriendRequests, requests -> requests.contains(currUserUid));
        isSentFriendRequest = Transformations.map(receivedFriendRequests, requests -> requests.contains(currUserUid));
    }

    private void setUpAttributeTransformations(LiveData<UserProfile> userProfile) {
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

    String getCurrUserUid() {
        return currUserUid;
    }

    public LiveData<Uri> getDisplayPicUri() {
        return displayPicUri;
    }

    public LiveData<List<Game>> getPastGames() {
        if (pastGames == null) {
            pastGames = loadGames(GameStatus.COMPLETED);
        }
        return pastGames;
    }

    public LiveData<List<Game>> getUpcomingGames() {
        if (upcomingGames == null) {
            upcomingGames = loadGames(GameStatus.PENDING);
        }
        return upcomingGames;
    }

    private LiveData<List<Game>> loadGames(GameStatus gameStatus) {
        return Transformations.switchMap(allGameIds, gameIds -> {
            MediatorLiveData<List<Game>> mediatorLiveData = new MediatorLiveData<>();
            List<String> pastGameIds = gameIds.get(gameStatus);
            Map<String, Game> games = new HashMap<>();

            if (pastGameIds != null) {
                for (String id : pastGameIds) {
                    mediatorLiveData.addSource(repo.getGame(id), game -> {
                        games.put(game.getUid(), game);
                        mediatorLiveData.postValue(new ArrayList<>(games.values()));
                    });
                }
            }
            mediatorLiveData.postValue(new ArrayList<>(games.values()));
            return mediatorLiveData;
        });
    }

    public LiveData<Integer> getNumGamesPlayed() {
        return numGamesPlayed;
    }

    private LiveData<List<UserProfile>> loadFriends() {
        return BatchTransformations.switchMapList(allFriendUids, repo::getUser, UserProfile::getUid);
    }

    public LiveData<List<UserProfile>> getFriends() {
        return allFriends;
    }

    public LiveData<Integer> getNumFriends() {
        return numFriends;
    }

    public LiveData<List<String>> getReceivedFriendRequests() {
        return receivedFriendRequests;
    }

    public LiveData<List<String>> getSentFriendRequests() {
        return sentFriendRequests;
    }

    public LiveData<Boolean> isReceivedFriendRequest() {
        return isReceivedFriendRequest;
    }

    public LiveData<Boolean> isSentFriendRequest() {
        return isSentFriendRequest;
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

    void addFriend() {
        repo.makeFriendRequest(currUserUid, mDisplayedUserUid);
    }

    void acceptFriendRequest() {
        repo.acceptFriendRequest(mDisplayedUserUid, currUserUid);
    }

    public void sendMessage() {
        GroupChannelParams params = new GroupChannelParams();
        params.addUserId(mDisplayedUserUid)
                .setDistinct(true)
                .setCustomType(SendBirdConstants.PRIVATE_CHAT_CUSTOM_TYPE);

        GroupChannel.createChannel(params, (groupChannel, e) -> {
            if (e != null) {
                sendMessageResult.postValue(new Result<>(e));
                return;
            }
            sendMessageResult.postValue(new Result<>(groupChannel));
        });
    }

    LiveData<Result<GroupChannel>> getSendMessageResult() {
        return sendMessageResult;
    }
}
