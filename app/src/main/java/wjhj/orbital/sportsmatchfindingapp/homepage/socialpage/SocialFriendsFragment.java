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
public class SocialFriendsFragment extends Fragment {


    public SocialFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.social_friends_fragment, container, false);
    }

}
