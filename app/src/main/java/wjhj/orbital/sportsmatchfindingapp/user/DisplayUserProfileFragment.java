package wjhj.orbital.sportsmatchfindingapp.user;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.auth.Authentications;
import wjhj.orbital.sportsmatchfindingapp.auth.LoginActivity;
import wjhj.orbital.sportsmatchfindingapp.databinding.DisplayUserProfileFragmentBinding;
import wjhj.orbital.sportsmatchfindingapp.game.GameActivity;
import wjhj.orbital.sportsmatchfindingapp.homepage.HomepageActivity;
import wjhj.orbital.sportsmatchfindingapp.homepage.gamespage.GamesCardAdapter;
import wjhj.orbital.sportsmatchfindingapp.homepage.socialpage.ChatPageActivity;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayUserProfileFragment extends Fragment implements FriendProfilesAdapter.UserProfileClickListener {

    public static final String DISPLAY_PROFILE_TAG = "display_profile: ";
    private static final String DISPLAYED_USER_UID_TAG = "user_uid";

    private DisplayUserProfileFragmentBinding binding;
    private DisplayUserProfileViewModel viewModel;
    private BottomNavigationView bottomNav;
    private String mDisplayedUserUid;

    public DisplayUserProfileFragment() {
        // Required empty public constructor
    }

    public static DisplayUserProfileFragment newInstance(String displayedUserUid) {
        DisplayUserProfileFragment fragment = new DisplayUserProfileFragment();
        Bundle args = new Bundle();
        args.putString(DISPLAYED_USER_UID_TAG, displayedUserUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDisplayedUserUid = getArguments().getString(DISPLAYED_USER_UID_TAG);
        }

        if (mDisplayedUserUid != null) {
            DisplayUserProfileViewModelFactory factory = new DisplayUserProfileViewModelFactory(mDisplayedUserUid);
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
        Toolbar toolbar = (Toolbar) binding.displayUserTopToolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(menuListener);


        initActionButton(binding.displayUserActionButton);

        initPreferencesRecyclerView(binding.displayUserProfilePreferences);

        initFriendProfilesRecyclerView(binding.displayUserFriendProfiles);

        initGamesCardRecyclerView(binding.displayUserGamesRecyclerView);

        viewModel.getSendMessageResult().observe(getViewLifecycleOwner(), groupChannelResult -> {
            if (groupChannelResult.isSuccessful()) {
                Intent intent = new Intent(requireContext(), ChatPageActivity.class);
                intent.putExtra(ChatPageActivity.CURR_USER_UID_TAG, viewModel.getCurrUserUid());
                intent.putExtra(ChatPageActivity.CHANNEL_URL_TAG, groupChannelResult.getResult().getUrl());

                requireActivity().startActivity(intent);
            }
        });

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
                        intent.putExtra(UserPreferencesActivity.EDIT_PROFILE_TAG, mDisplayedUserUid);
                        startActivity(intent);
                    },
                    true);
            actionButton.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        } else {
            viewModel.isFriend().observe(getViewLifecycleOwner(), isFriend -> {
                if (isFriend) {
                    updateButton(actionButton,
                            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
                            requireActivity().getString(R.string.display_profile_friends),
                            v -> {},
                            false);
                } else {
                    updateButton(actionButton,
                            ContextCompat.getColor(requireActivity(), R.color.green),
                            requireActivity().getString(R.string.display_user_add_friend),
                            v-> viewModel.addFriend(),
                            true);
                }
            });

            viewModel.isReceivedFriendRequest().observe(getViewLifecycleOwner(), received -> {
                if (received) {
                    updateButton(actionButton,
                            ContextCompat.getColor(requireActivity(), R.color.green),
                            requireActivity().getString(R.string.display_user_accept_friend_request),
                            v -> viewModel.acceptFriendRequest(),
                            true);

                }
            });

            viewModel.isSentFriendRequest().observe(getViewLifecycleOwner(), sent -> {
                if (sent) {
                    updateButton(actionButton,
                            ContextCompat.getColor(requireActivity(), R.color.gray),
                            requireActivity().getString(R.string.display_profile_friend_request_pending),
                            v -> {},
                            false);
                }
            });
        }
    }

    private void updateButton(Button button, int color, CharSequence text,
                              View.OnClickListener onClickListener, boolean enabled) {
        button.setBackgroundColor(color);
        button.setText(text);
        button.setOnClickListener(onClickListener);
        button.setEnabled(enabled);
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
        viewModel.getFriends().observe(getViewLifecycleOwner(), adapter::updateFriends);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initGamesCardRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        GamesCardAdapter adapter = new GamesCardAdapter(game -> {
            Intent intent = new Intent(requireActivity(), GameActivity.class);
            intent.putExtra(GameActivity.GAME_UID, game.getUid());
            startActivity(intent);
        });

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
        FragmentManager manager = requireFragmentManager();
        manager.beginTransaction()
                .replace(android.R.id.content, DisplayUserProfileFragment.newInstance(uid))
                .addToBackStack(null)
                .commit();
    }

    private Toolbar.OnMenuItemClickListener menuListener = item -> {
        switch (item.getItemId()) {
            case R.id.options_profile:
                openProfilePage();
                break;
            case R.id.options_logout:
                logOut();
                break;
        }
        return true;
    };

    private void openProfilePage() {
        FragmentManager manager = requireFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(HomepageActivity.DISPLAY_PROFILE_PIC_TAG);

        if (fragment == null) {
            transaction.add(android.R.id.content,
                    DisplayUserProfileFragment.newInstance(FirebaseAuth.getInstance().getUid()),
                    HomepageActivity.DISPLAY_PROFILE_PIC_TAG)
                    .addToBackStack(null);
        } else {
            transaction.replace(android.R.id.content,
                    fragment, HomepageActivity.DISPLAY_PROFILE_PIC_TAG);
        }
        transaction.commit();
    }

    private void logOut() {
        Authentications auths = new Authentications();
        auths.logOutFirebase();
        auths.logOutGoogle(requireContext());
        SportalRepo.getInstance().refreshCache();
        Intent logoutIntent = new Intent(requireContext(), LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(logoutIntent);
        requireFragmentManager().popBackStack();
    }
}
