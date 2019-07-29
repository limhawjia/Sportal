package wjhj.orbital.sportsmatchfindingapp.messaging;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.sendbird.android.Sender;
import com.sendbird.android.UserMessage;

import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public class SportalUserMessage {
    private UserMessage userMessage;
    private LiveData<String> displayName;
    private LiveData<Uri> displayPicUri;
    private boolean isFullView;

    public SportalUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
        SportalRepo repo = SportalRepo.getInstance();
        LiveData<UserProfile> userProfile = repo.getUser(userMessage.getSender().getUserId());
        displayName = Transformations.map(userProfile, UserProfile::getDisplayName);
        displayPicUri = Transformations.map(userProfile, UserProfile::getDisplayPicUri);
    }

    public String getMessageText() {
        return userMessage.getMessage();
    }

    public LiveData<String> getDisplayName() {
        return displayName;
    }

    public LiveData<Uri> getDisplayPicUri() {
        return displayPicUri;
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }

    public Sender getSender() {
        return userMessage.getSender();
    }

    public long getCreatedAt() {
        return userMessage.getCreatedAt();
    }

    public boolean isFullView() {
        return isFullView;
    }

    public void setFullView(boolean fullView) {
        isFullView = fullView;
    }
}
