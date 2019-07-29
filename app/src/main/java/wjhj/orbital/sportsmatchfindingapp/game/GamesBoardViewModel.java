package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.common.base.Optional;
import com.google.firebase.auth.FirebaseAuth;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.messaging.SendBirdConnectionManager;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class GamesBoardViewModel extends ViewModel {

    private static final String CONNECTION_HANDLER = "connection_handler";
    private static final String GROUP_CHAT_HANDLER_ID = "group_chat_handler";
    private static final int CHAT_MESSAGES_LIMIT = 30;

    private final SportalRepo repo;
    private String mGameUid;
    private LiveData<Game> currGame;
    private LiveData<Boolean> isVerifiedUser;
    private MutableLiveData<Exception> connectionExceptions;
    private MutableLiveData<String> messageTextLiveData;
    private LiveData<Boolean> sendButtonEnabled;
    private MutableLiveData<GroupChannel> groupChannelLiveData;
    private MediatorLiveData<List<BaseMessage>> messagesLiveData;

    private boolean mIsMessageListLoading;

    public GamesBoardViewModel(String gameUid) {
        mGameUid = gameUid;
        repo = SportalRepo.getInstance();
        currGame = repo.getGame(gameUid);
        isVerifiedUser = Transformations.map(currGame, game -> {
            String currUserUid = FirebaseAuth.getInstance().getUid();
            return game.getCreatorUid().equals(currUserUid)
                    || game.getParticipatingUids().contains(currUserUid);
        });
        connectionExceptions = new MutableLiveData<>();
        messageTextLiveData = new MutableLiveData<>();
        sendButtonEnabled = Transformations.map(messageTextLiveData, text -> text != null && text.length() > 0);
        groupChannelLiveData = new MutableLiveData<>();
        messagesLiveData = new MediatorLiveData<>();
        messagesLiveData.addSource(groupChannelLiveData, this::loadLatestMessages);

        setUpConnectionManger();
        tryLoadChannel();
        setUpChannelHandler();
    }


    private void setUpConnectionManger() {
        SendBirdConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER,
                reconnect -> {
                    Timber.d("Reconnected to games board");
                    tryLoadChannel();
                    setUpChannelHandler();
                });
    }

    private void tryLoadChannel() {
        isVerifiedUser.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    loadChannel();
                }
                isVerifiedUser.removeObserver(this);
            }
        });
    }

    private void loadChannel() {
        if (currGame.getValue() == null) {
            return;
        }

        Optional<String> groupChannelUrl = currGame.getValue().getGameBoardChannelUrl();

        if (groupChannelUrl.isPresent()) {
            GroupChannel.getChannel(groupChannelUrl.get(), (groupChannel, e1) -> {
                if (e1 != null) {
                    connectionExceptions.postValue(e1);
                    Timber.d(e1, "Get games channel error");
                    return;
                }

                groupChannel.join(e2 -> {
                    if (e2 != null) {
                        connectionExceptions.postValue(e2);
                        Timber.d(e2, "Join game channel error");
                        return;
                    }

                    groupChannelLiveData.postValue(groupChannel);
                });
            });

        } else {

            SendBirdConnectionManager.createGameBoardChannel(mGameUid)
                    .addOnSuccessListener(groupChannel -> {
                        groupChannel.join(e -> {
                            if (e != null) {
                                connectionExceptions.postValue(e);
                                Timber.d(e, "Join game channel error");
                                return;
                            }
                            groupChannelLiveData.postValue(groupChannel);
                        });
                        repo.updateGame(currGame.getValue().getUid(),
                                currGame.getValue().withGameBoardChannelUrl(groupChannel.getUrl()));
                    })
                    .addOnFailureListener(connectionExceptions::postValue);
        }
    }

    private void setUpChannelHandler() {
        groupChannelLiveData.observeForever(new Observer<GroupChannel>() {
            @Override
            public void onChanged(GroupChannel groupChannel) {
                Timber.d("Handler set up");
                SendBird.addChannelHandler(GROUP_CHAT_HANDLER_ID, new SendBird.ChannelHandler() {
                    @Override
                    public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                        if (baseChannel.getUrl().equals(groupChannel.getUrl())) {
                            addMessage(baseMessage);
                        }
                    }
                });

                groupChannelLiveData.removeObserver(this);
            }
        });


    }

    private void loadLatestMessages(GroupChannel groupChannel) {
        if (isMessageListLoading()) {
            return;
        }

        setMessageListLoading(true);
        groupChannel.getPreviousMessagesByTimestamp(Long.MAX_VALUE, true, CHAT_MESSAGES_LIMIT,
                true, BaseChannel.MessageTypeFilter.ALL, null,
                (list, e) -> {
                    setMessageListLoading(false);
                    if (e != null) {
                        Timber.d(e, "Get messagesLiveData error");
                        return;
                    }
                    groupChannel.markAsRead();
                    messagesLiveData.postValue(list);
                }
        );
    }

    void loadPreviousMessages() {
        if (isMessageListLoading()) {
            return;
        }

        if (groupChannelLiveData.getValue() != null) {
            GroupChannel channel = groupChannelLiveData.getValue();

            long oldestMessageCreatedAt = Long.MAX_VALUE;
            List<BaseMessage> messages = messagesLiveData.getValue();
            if (messages != null && messages.size() > 0) {
                oldestMessageCreatedAt = messages.get(messages.size() - 1).getCreatedAt();
            }

            setMessageListLoading(true);
            channel.getPreviousMessagesByTimestamp(oldestMessageCreatedAt, false, CHAT_MESSAGES_LIMIT,
                    true, BaseChannel.MessageTypeFilter.ALL, null,
                    (list, e) -> {
                        setMessageListLoading(false);
                        if (e != null) {
                            Timber.d(e, "Get messages error");
                            return;
                        }

                        appendMessagesEnd(list);
                    });
        }
    }

    public void sendMessage(String messageText) {
        Timber.d(messageText);
        GroupChannel channel = groupChannelLiveData.getValue();
        if (channel == null) {
            return;
        }

        UserMessage tempUserMessage = channel.sendUserMessage(messageText,
                (userMessage, e) -> {
                    if (e != null) {
                        Timber.d(e, "Send message failed");
                        removeFailedMessage(userMessage);
                        return;
                    }
                    markMessageSent(userMessage);

                });
        messageTextLiveData.setValue("");
        addMessage(tempUserMessage);
    }

    private void addMessage(BaseMessage message) {
        if (messagesLiveData.getValue() == null) {
            messagesLiveData.postValue(Collections.singletonList(message));
        } else {
            List<BaseMessage> messages = new ArrayList<>(messagesLiveData.getValue());
            messages.add(0, message);
            messagesLiveData.postValue(messages);
        }
    }

    private void appendMessagesEnd(List<BaseMessage> messages) {
        if (messagesLiveData.getValue() == null) {
            messagesLiveData.postValue(messages);
        } else {
            List<BaseMessage> existingMessages = new ArrayList<>(messagesLiveData.getValue());
            existingMessages.addAll(messages);
            messagesLiveData.postValue(existingMessages);
        }
    }


    private void markMessageSent(BaseMessage message) {
        List<BaseMessage> messages = messagesLiveData.getValue();
        if (messages != null) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                BaseMessage msg = messages.get(i);
                if (message instanceof UserMessage && msg instanceof UserMessage) {

                    if (((UserMessage) msg).getRequestId().equals(((UserMessage) message).getRequestId())) {
                        messages.set(i, message);
                        messagesLiveData.postValue(messages);
                        return;
                    }
                }
            }
        }
    }

    private void removeFailedMessage(BaseMessage message) {
        List<BaseMessage> messages = messagesLiveData.getValue();
        if (messages != null && message instanceof UserMessage) {
            messages.remove(message);
            messagesLiveData.postValue(messages);
        }
    }

    LiveData<List<BaseMessage>> getMessagesLiveData() {
        return messagesLiveData;
    }

    LiveData<Exception> getConnectionExceptions() {
        return connectionExceptions;
    }

    public MutableLiveData<String> getMessageTextLiveData() {
        return messageTextLiveData;
    }

    public LiveData<Boolean> getSendButtonEnabled() {
        return sendButtonEnabled;
    }

    // Verified means either participating or the creator.
    public LiveData<Boolean> isVerifiedUser() {
        return isVerifiedUser;
    }

    private synchronized boolean isMessageListLoading() {
        return mIsMessageListLoading;
    }

    public synchronized void setMessageListLoading(boolean isMessageListLoading) {
        mIsMessageListLoading = isMessageListLoading;
    }
}
