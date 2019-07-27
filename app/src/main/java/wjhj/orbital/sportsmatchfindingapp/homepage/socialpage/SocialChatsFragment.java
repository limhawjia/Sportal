package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import wjhj.orbital.sportsmatchfindingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialChatsFragment extends Fragment {
    private static final String CURR_USER_UID_TAG = "curr_user_uid";

    public SocialChatsFragment() {
        // Required empty public constructor
    }

    public static SocialChatsFragment newInstance() {
        return new SocialChatsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.social_chats_fragment, container, false);


        return view;
    }

}
