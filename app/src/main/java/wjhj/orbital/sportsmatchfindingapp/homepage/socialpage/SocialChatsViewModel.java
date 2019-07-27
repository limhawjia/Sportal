package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;

import java.util.Collections;
import java.util.List;

import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;
import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.messaging.PrivateChat;
import wjhj.orbital.sportsmatchfindingapp.messaging.SendbirdConstants;

public class SocialChatsViewModel extends ViewModel {

    private String currUserUid;

    private MutableLiveData<List<PrivateChat>> privateChats;

    public SocialChatsViewModel(String currUserUid) {
        this.currUserUid = currUserUid;
        this.privateChats = new MutableLiveData<>();
        loadGroupChannels();
    }

    private void loadGroupChannels() {
        GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
        channelListQuery.setIncludeEmpty(true);
        channelListQuery.setOrder(GroupChannelListQuery.Order.LATEST_LAST_MESSAGE);
        channelListQuery.setCustomTypesFilter(Collections.singletonList(SendbirdConstants.PRIVATE_CHAT_CUSTOM_TYPE));
        channelListQuery.next((list, e) -> {
            if (e != null) {
                Timber.d(e, "Load channels error");
                return;
            }
            privateChats.setValue(StreamSupport.stream(list)
                    .map(channel -> PrivateChat.of(channel, currUserUid))
                    .collect(Collectors.toList()));
        });
    }

    LiveData<List<PrivateChat>> getPrivateChats() {
        return privateChats;
    }
}
