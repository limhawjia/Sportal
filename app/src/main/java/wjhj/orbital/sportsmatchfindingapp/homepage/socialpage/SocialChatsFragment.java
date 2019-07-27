package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelParams;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialChatsFragment extends Fragment {
    private static final String CURR_USER_UID_TAG = "curr_user_uid";

    private String mUserUid;


    public SocialChatsFragment() {
        // Required empty public constructor
    }

    public static SocialChatsFragment newInstance(String userUid) {
        SocialChatsFragment fragment = new SocialChatsFragment();
        Bundle args = new Bundle();
        args.putString(CURR_USER_UID_TAG, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUserUid = getArguments().getString(CURR_USER_UID_TAG);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.social_chats_fragment, container, false);
        TextView textView = view.findViewById(R.id.TEST_TEST);
        Button button = view.findViewById(R.id.TEST_BUTTON);

        button.setOnClickListener(v -> {
            GroupChannelParams params = new GroupChannelParams()
                    .setPublic(false)
                    .setDistinct(true)
                    .addUserId("9EdldWC2b3ZW0z13UwGBPJ6a5Vv1")
                    .setCustomType("private_chat");
            GroupChannel.createChannel(params, (groupChannel, e) -> {
                if (e != null) {
                    Timber.d(e, "Create channel failure");
                } else {
                    Timber.d(groupChannel.getMembers().toString());
                    Timber.d(groupChannel.getUrl());
                }
            });
        });


        return view;
    }

}
