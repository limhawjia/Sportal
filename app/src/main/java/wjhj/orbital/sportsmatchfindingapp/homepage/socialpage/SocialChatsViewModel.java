package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;

import java.util.Collections;
import java.util.List;

import java9.util.stream.Collectors;
import java9.util.stream.Stream;
import java9.util.stream.StreamSupport;
import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.messaging.PrivateChat;
import wjhj.orbital.sportsmatchfindingapp.messaging.SendBirdConnectionManager;
import wjhj.orbital.sportsmatchfindingapp.messaging.SendBirdConstants;

public class SocialChatsViewModel extends ViewModel {
    private static final String CONNECTION_HANDLER = "connection_handler";
    private static final int CHAT_LIST_LIMIT = 15;

    private String currUserUid;

    private GroupChannelListQuery mChannelsQuery;
    private MutableLiveData<List<PrivateChat>> privateChats;

    public SocialChatsViewModel(String currUserUid) {
        this.currUserUid = currUserUid;
        this.privateChats = new MutableLiveData<>();
        setUpConnectionManger();
        loadGroupChannels();
    }

    private void setUpConnectionManger() {
        SendBirdConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER,
                reconnect -> loadGroupChannels());
    }

    private void loadGroupChannels() {
        mChannelsQuery = GroupChannel.createMyGroupChannelListQuery();
        mChannelsQuery.setIncludeEmpty(true);
        mChannelsQuery.setOrder(GroupChannelListQuery.Order.LATEST_LAST_MESSAGE);
        mChannelsQuery.setCustomTypesFilter(Collections.singletonList(SendBirdConstants.PRIVATE_CHAT_CUSTOM_TYPE));
        mChannelsQuery.setLimit(CHAT_LIST_LIMIT);
        mChannelsQuery.next((list, e) -> {
            if (e != null) {
                Timber.d(e, "Load channels error");
                return;
            }
            privateChats.postValue(StreamSupport.stream(list)
                    .map(channel -> PrivateChat.of(channel, currUserUid))
                    .collect(Collectors.toList()));
        });
    }

    void loadNextChannels() {
        if (mChannelsQuery != null) {
            mChannelsQuery.next((list, e) -> {
                if (e != null) {
                    Timber.d(e, "Load channels error");
                    return;
                }

                List<PrivateChat> currentChats = privateChats.getValue();
                if (currentChats == null) {
                    privateChats.postValue(StreamSupport.stream(list)
                            .map(channel -> PrivateChat.of(channel, currUserUid))
                            .collect(Collectors.toList()));
                } else {
                    privateChats.postValue(Stream.concat(StreamSupport.stream(currentChats),
                            StreamSupport.stream(list)
                                    .map(channel -> PrivateChat.of(channel, currUserUid)))
                            .collect(Collectors.toList())
                    );
                }
            });
        }

    }

    LiveData<List<PrivateChat>> getPrivateChats() {
        return privateChats;
    }

    void refreshChats() {
        List<PrivateChat> chats = privateChats.getValue();
        if (chats != null) {
            for (PrivateChat chat : chats) {
                chat.updateUnreadMessages();
            }
        }
    }
}
