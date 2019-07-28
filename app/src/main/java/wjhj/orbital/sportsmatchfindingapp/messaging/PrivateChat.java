package wjhj.orbital.sportsmatchfindingapp.messaging;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBird;
import com.sendbird.android.UserMessage;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;
import wjhj.orbital.sportsmatchfindingapp.utils.DateUtils;

public class PrivateChat {

    private final GroupChannel groupChannel;
    private final String currUserUid;
    private LiveData<UserProfile> friendProfile;
    private MutableLiveData<UserMessage> lastSentUserMessage;

    private PrivateChat(GroupChannel groupChannel, String currUserUid, String friendUserUid) {
        SportalRepo repo = SportalRepo.getInstance();
        this.groupChannel = groupChannel;
        this.currUserUid = currUserUid;
        this.friendProfile = repo.getUser(friendUserUid);
    }

    public static PrivateChat of(GroupChannel groupChannel, String currUserUid) {
        List<Member> members = groupChannel.getMembers();
        if (members.size() != 2) {
            throw new IllegalArgumentException("Private chat must be between 2 users");
        }

        if (members.get(0).getUserId().equals(currUserUid)) {
            return new PrivateChat(groupChannel, currUserUid, members.get(1).getUserId());

        } else if (members.get(1).getUserId().equals(currUserUid)) {
            return new PrivateChat(groupChannel, currUserUid, members.get(0).getUserId());

        } else {
            throw new IllegalArgumentException("User not part of private chat");
        }
    }

    public GroupChannel getChannel() {
        return groupChannel;
    }

    public LiveData<String> getFriendUid() {
        return Transformations.map(friendProfile, UserProfile::getUid);
    }

    public LiveData<String> getFriendDisplayName() {
        return Transformations.map(friendProfile, UserProfile::getDisplayName);
    }

    public LiveData<Uri> getFriendDisplayPicUri() {
        return Transformations.map(friendProfile, UserProfile::getDisplayPicUri);
    }

    public LiveData<UserMessage> loadLastSent() {
        if (lastSentUserMessage == null) {
            lastSentUserMessage = new MutableLiveData<>();
            SendBird.addChannelHandler(currUserUid, new SendBird.ChannelHandler() {
                @Override
                public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                    if (baseChannel.getUrl().equals(groupChannel.getUrl())) {
                        if (baseMessage instanceof UserMessage) {
                            lastSentUserMessage.postValue((UserMessage) baseMessage);
                        }
                    }

                }
            });

            BaseMessage lastMessage = groupChannel.getLastMessage();
            if (lastMessage instanceof UserMessage) {
                lastSentUserMessage.postValue((UserMessage) lastMessage);
            }
        }

        return lastSentUserMessage;
    }

    public LiveData<String> getLastSentUserMessage() {
        return Transformations.map(loadLastSent(), UserMessage::getMessage);
    }

    public LiveData<String> getLastSentTime() {
        return Transformations.map(loadLastSent(), message ->
                DateUtils.formatDateTimeForChat(message.getCreatedAt()));
    }


    public LiveData<Integer> getUnreadMessageCount() {
        MutableLiveData<Integer> countLiveData = new MutableLiveData<>();
        countLiveData.setValue(groupChannel.getUnreadMessageCount());
        SendBird.addChannelHandler(currUserUid, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals(groupChannel.getUrl())) {
                    countLiveData.postValue(((GroupChannel) baseChannel).getUnreadMessageCount());
                }
            }
        });
        return countLiveData;
    }
}
