package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;

import android.view.inputmethod.InputMethodManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.messaging.PrivateChat;
import wjhj.orbital.sportsmatchfindingapp.messaging.SendBirdConnectionManager;

public class ChatPageViewModel extends ViewModel {
    private static final String CONNECTION_HANDLER = "connection_handler";
    private static final String PRIVATE_CHAT_HANDLER_ID = "private_chat_handler";
    private static final int CHAT_MESSAGES_LIMIT = 30;

    private String mCurrUserUid;
    private String mChannelUrl;
    private boolean mIsMessageListLoading;
    private InputMethodManager mIMM;
    private MutableLiveData<PrivateChat> chatLiveData;
    private MutableLiveData<String> messageTextLiveData;
    private LiveData<Boolean> sendButtonEnabled;
    private MediatorLiveData<List<BaseMessage>> messagesLiveData;


    public ChatPageViewModel(String currUserUid, String channelUrl, InputMethodManager iMM) {
        mCurrUserUid = currUserUid;
        mChannelUrl = channelUrl;
        mIMM = iMM;
        chatLiveData = new MutableLiveData<>();
        messageTextLiveData = new MutableLiveData<>();
        sendButtonEnabled = Transformations.map(messageTextLiveData, text -> text != null && text.length() > 0);
        messagesLiveData = new MediatorLiveData<>();

        setUpConnectionManger();
        refresh();
        setUpChannelHandler();

        messagesLiveData.addSource(chatLiveData, this::loadLatestMessages);

    }

    private void setUpConnectionManger() {
        SendBirdConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER,
                reconnect -> {
                    Timber.d("Reconnected to private chat");
                    refresh();
                    setUpChannelHandler();
                });
    }

    private void setUpChannelHandler() {
        SendBird.addChannelHandler(PRIVATE_CHAT_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    addMessage(baseMessage);
                }
            }
        });
    }

    private void refresh() {
        if (chatLiveData.getValue() == null) {
            GroupChannel.getChannel(mChannelUrl, (groupChannel, e) -> {
                if (e != null) {
                    Timber.d(e, "Get channel error");
                    return;
                }
                chatLiveData.postValue(PrivateChat.of(groupChannel, mCurrUserUid));
            });
        } else {
            GroupChannel channel = chatLiveData.getValue().getChannel();
            channel.refresh(e -> {
                if (e != null) {
                    Timber.d(e, "Get channel error");
                    return;
                }
                chatLiveData.postValue(PrivateChat.of(channel, mCurrUserUid));
            });
        }
    }

    private void loadLatestMessages(PrivateChat chat) {
        if (isMessageListLoading()) {
            return;
        }

        setMessageListLoading(true);
        chat.getChannel().getPreviousMessagesByTimestamp(Long.MAX_VALUE, true, CHAT_MESSAGES_LIMIT,
                true, BaseChannel.MessageTypeFilter.ALL, null,
                (list, e) -> {
                    setMessageListLoading(false);
                    if (e != null) {
                        Timber.d(e, "Get messages error");
                        return;
                    }
                    chat.getChannel().markAsRead();
                    messagesLiveData.postValue(list);
                }
        );
    }

    void loadPreviousMessages() {
        if (isMessageListLoading()) {
            return;
        }

        if (chatLiveData.getValue() != null) {
            GroupChannel channel = chatLiveData.getValue().getChannel();

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
        PrivateChat chat = chatLiveData.getValue();
        if (chat == null) {
            return;
        }

        UserMessage tempUserMessage = chat.getChannel().sendUserMessage(messageText,
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

    LiveData<PrivateChat> getChatLiveData() {
        return chatLiveData;
    }

    LiveData<List<BaseMessage>> getMessagesLiveData() {
        return messagesLiveData;
    }

    public MutableLiveData<String> getMessageTextLiveData() {
        return messageTextLiveData;
    }

    public LiveData<Boolean> getSendButtonEnabled() {
        return sendButtonEnabled;
    }

    private synchronized boolean isMessageListLoading() {
        return mIsMessageListLoading;
    }

    private synchronized void setMessageListLoading(boolean isMessageListLoading) {
        mIsMessageListLoading = isMessageListLoading;
    }
}
