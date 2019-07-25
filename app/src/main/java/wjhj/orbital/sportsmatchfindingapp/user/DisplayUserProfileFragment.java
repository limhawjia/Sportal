package wjhj.orbital.sportsmatchfindingapp.user;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.DisplayUserProfileFragmentBinding;
import wjhj.orbital.sportsmatchfindingapp.homepage.gamespage.GamesCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayUserProfileFragment extends Fragment implements FriendProfilesAdapter.UserProfileClickListener {

    private static String USER_UID_TAG = "user_uid";

    private DisplayUserProfileFragmentBinding binding;
    private DisplayUserProfileViewModel viewModel;
    private BottomNavigationView bottomNav;
    private String mUserUid;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bottomNav = requireActivity().findViewById(R.id.bottom_nav);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.INVISIBLE);
        }

        binding = DisplayUserProfileFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        initActionButton(binding.displayUserActionButton);

        initPreferencesRecyclerView(binding.displayUserProfilePreferences);

        initFriendProfilesRecyclerView(binding.displayUserFriendProfiles);

        initGamesCardRecyclerView(binding.displayUserGamesRecyclerView);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }

    private void initActionButton(Button actionButton) {
        if (viewModel.isCurrentUser()) {
            updateButton(actionButton,
                    ContextCompat.getColor(requireActivity(), R.color.offWhite),
                    requireActivity().getString(R.string.user_profile_edit_profile),
                    v -> {
                        Intent intent = new Intent(getContext(), UserPreferencesActivity.class);
                        intent.putExtra(UserPreferencesActivity.EDIT_PROFILE_TAG, mUserUid);
                        startActivity(intent);
                    });
            actionButton.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        } else {
            viewModel.isFriend().observe(getViewLifecycleOwner(), isFriend -> {
                if (isFriend) {
                    updateButton(actionButton,
                            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
                            requireActivity().getString(R.string.display_profile_friends),
                            v -> {});
                } else {
                    updateButton(actionButton,
                            ContextCompat.getColor(requireActivity(), R.color.green),
                            requireActivity().getString(R.string.display_user_add_friend),
                            v-> {});
                }
            });
        }
    }

    private void updateButton(Button button, int color, CharSequence text, View.OnClickListener onClickListener) {
        button.setBackgroundColor(color);
        button.setText(text);
        button.setOnClickListener(onClickListener);
    }

    private void initPreferencesRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                RecyclerView.HORIZONTAL, false);

        PreferencesIconAdapter adapter = new PreferencesIconAdapter();
        viewModel.getPreferences().observe(getViewLifecycleOwner(), adapter::updatePreferences);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initFriendProfilesRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                RecyclerView.HORIZONTAL, false);

        FriendProfilesAdapter adapter = new FriendProfilesAdapter(this);
        viewModel.getFriends().observe(getViewLifecycleOwner(), friends -> {
            Log.d("TESTINGG", friends.toString());
            adapter.updateFriends(friends);
        });

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initGamesCardRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        GamesCardAdapter adapter = new GamesCardAdapter();

        if (viewModel.isCurrentUser()) {
            viewModel.getPastGames().observe(getViewLifecycleOwner(), games -> {
                Collections.sort(games, (game1, game2) -> game2.getStartDateTime().compareTo(game1.getStartDateTime()));
                adapter.updateGames(games);
            });
        } else {
            viewModel.getUpcomingGames().observe(getViewLifecycleOwner(), games -> {
                Collections.sort(games, (game1, game2) -> game1.getStartDateTime().compareTo(game2.getStartDateTime()));
                adapter.updateGames(games);
            });
        }

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onUserProfileClick(String uid) {
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .replace(((ViewGroup) requireView().getParent()).getId(), DisplayUserProfileFragment.newInstance(uid))
                .addToBackStack(null)
                .commit();
    }
}
