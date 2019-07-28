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
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.messaging.PrivateChat;

public class ChatPageViewModel extends ViewModel {
    private static final String PRIVATE_CHAT_HANDLER_ID = "private_chat_handler";
    private static final int CHAT_LIST_LIMIT = 30;

    private String mCurrUserUid;
    private String mChannelUrl;
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

        loadChannel();

        messagesLiveData.addSource(chatLiveData, this::loadLatestMessages);

    }


    private void loadChannel() {
        if (chatLiveData.getValue() == null) {
            GroupChannel.getChannel(mChannelUrl, (groupChannel, e) -> {
                if (e != null) {
                    Timber.d(e, "Get channel error");
                    return;
                }
                chatLiveData.postValue(PrivateChat.of(groupChannel, mCurrUserUid));
            });
        }
    }

    private void loadLatestMessages(PrivateChat chat) {
        chat.getChannel().getPreviousMessagesByTimestamp(Long.MAX_VALUE, true, CHAT_LIST_LIMIT, false,
                BaseChannel.MessageTypeFilter.ALL, null,
                (list, e) -> {
                    if (e != null) {
                        Timber.d(e, "Get messages error");
                        return;
                    }
                    chat.getChannel().markAsRead();
                    messagesLiveData.postValue(list);
                }
        );
    }

    LiveData<PrivateChat> getChatLiveData() {
        return chatLiveData;
    }

    LiveData<List<BaseMessage>> getMessagesLiveData() {
        return messagesLiveData;
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
            messages.add(message);
            messagesLiveData.postValue(messages);
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

    public MutableLiveData<String> getMessageTextLiveData() {
        return messageTextLiveData;
    }

    public LiveData<Boolean> getSendButtonEnabled() {
        return sendButtonEnabled;
    }
}
