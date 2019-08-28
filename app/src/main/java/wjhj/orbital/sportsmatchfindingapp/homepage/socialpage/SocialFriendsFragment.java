package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wjhj.orbital.sportsmatchfindingapp.databinding.SocialFriendsFragmentBinding;
import wjhj.orbital.sportsmatchfindingapp.user.DisplayUserProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialFriendsFragment extends Fragment implements FriendsCardAdapter.FriendsCardListener {
    private static final String CURR_USER_UID_TAG = "curr_user_uid";

    private SocialFriendsViewModel viewModel;
    private SocialFriendsFragmentBinding binding;
    private String currUserUid;
    private FriendsCardAdapter mAdapter;


    public SocialFriendsFragment() {
        // Required empty public constructor
    }

    public static SocialFriendsFragment newInstance(String currUserUid) {
        SocialFriendsFragment fragment = new SocialFriendsFragment();
        Bundle args = new Bundle();
        args.putString(CURR_USER_UID_TAG, currUserUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currUserUid = getArguments().getString(CURR_USER_UID_TAG);
        }

        SocialViewModelFactory factory = new SocialViewModelFactory(currUserUid);
        viewModel = ViewModelProviders.of(this, factory).get(SocialFriendsViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = SocialFriendsFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        initFriendsRecyclerView(binding.socialFriendsList);

        return binding.getRoot();
    }

    private void initFriendsRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        mAdapter = new FriendsCardAdapter(this,
                viewModel.getCurrUserPreferencesTask());

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        viewModel.getFriends().observe(getViewLifecycleOwner(), mAdapter::submitCurrentFriendsList);

        viewModel.getSearchedProfiles().observe(getViewLifecycleOwner(), mAdapter::submitSearchList);
        viewModel.getSearchText().observe(getViewLifecycleOwner(), text -> {
            if (text.isEmpty()) {
                mAdapter.revertToCurrentFriendsView();
            }
        });
    }


    @Override
    public void onCardClicked(String profileUid) {
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String tag = DisplayUserProfileFragment.DISPLAY_PROFILE_TAG + profileUid;
        Fragment fragment = manager
                .findFragmentByTag(tag);

        if (fragment == null) {
            transaction.add(android.R.id.content,
                    DisplayUserProfileFragment.newInstance(profileUid),
                    tag)
                    .addToBackStack(null);
        } else {
            transaction.replace(android.R.id.content,
                    fragment, tag);
        }
        transaction.commit();
    }
}
