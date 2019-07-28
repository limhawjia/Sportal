package wjhj.orbital.sportsmatchfindingapp.messaging;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelParams;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import timber.log.Timber;

public class SendBirdConnectionManager {

    public static void login(String userId, final SendBird.ConnectHandler handler) {
        SendBird.connect(userId, (user, e) -> {
            if (handler != null) {
                handler.onConnected(user, e);
            }
        });
    }

    public static void logout(final SendBird.DisconnectHandler handler) {
        SendBird.disconnect(() -> {
            if (handler != null) {
                handler.onDisconnected();
            }
        });
    }

    public static void addConnectionManagementHandler(String handlerId, @NonNull final ConnectionManagementHandler handler) {
        SendBird.addConnectionHandler(handlerId, new SendBird.ConnectionHandler() {
            @Override
            public void onReconnectStarted() {
            }

            @Override
            public void onReconnectSucceeded() {
                handler.onConnected(true);
            }

            @Override
            public void onReconnectFailed() {
            }
        });

        if (SendBird.getConnectionState() == SendBird.ConnectionState.OPEN) {
            handler.onConnected(false);

        } else if (SendBird.getConnectionState() == SendBird.ConnectionState.CLOSED) { // push notification or system kill
            FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currUser != null) {
                SendBird.connect(currUser.getUid(), (user, e) -> {
                    if (e != null) {
                        return;
                    }
                    handler.onConnected(false);
                });
            }
        }
    }

    public static Task<GroupChannel> createGameBoardChannel(String gameUid) {
        TaskCompletionSource<GroupChannel> channelSource = new TaskCompletionSource<>();

        GroupChannelParams params = new GroupChannelParams()
                .setData(gameUid)
                .setPublic(true)
                .setDistinct(false)
                .setCustomType(SendBirdConstants.GROUP_CHAT_CUSTOM_TYPE);

        GroupChannel.createChannel(params, (groupChannel, e) -> {
            if (e != null) {
                Timber.d(e, "Failed to create channel");
                channelSource.setException(e);
            }
            channelSource.setResult(groupChannel);
        });

        return channelSource.getTask();
    }

    public static void removeConnectionManagementHandler(String handlerId) {
        SendBird.removeConnectionHandler(handlerId);
    }

    public interface ConnectionManagementHandler {
        /**
         * A callback for when connected or reconnected to refresh.
         *
         * @param reconnect Set false if connected, true if reconnected.
         */
        void onConnected(boolean reconnect);
    }
}