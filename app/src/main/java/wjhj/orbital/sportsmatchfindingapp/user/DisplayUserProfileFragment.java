package wjhj.orbital.sportsmatchfindingapp.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import wjhj.orbital.sportsmatchfindingapp.databinding.DisplayUserProfileFragmentBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayUserProfileFragment extends Fragment {

    private static String USER_UID_TAG = "user_uid";

    private DisplayUserProfileFragmentBinding binding;
    private DisplayUserProfileViewModel viewModel;
    private String mUserUid;
    private Uri displayPicUri;

    public DisplayUserProfileFragment() {
        // Required empty public constructor
    }

    public static DisplayUserProfileFragment newInstance(String userUid) {
        DisplayUserProfileFragment fragment = new DisplayUserProfileFragment();
        Bundle args = new Bundle();
        args.putString(USER_UID_TAG, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUserUid = getArguments().getString(USER_UID_TAG);
        }

        if (mUserUid != null) {
            DisplayUserProfileViewModelFactory factory = new DisplayUserProfileViewModelFactory(mUserUid);
            viewModel = ViewModelProviders.of(this, factory)
                    .get(DisplayUserProfileViewModel.class);
        } else {
            Toast.makeText(getActivity(), "Error occurred, please try again", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DisplayUserProfileFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }


}
