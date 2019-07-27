package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.DisplayUserProfileFragment;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;
import wjhj.orbital.sportsmatchfindingapp.utils.BatchTransformations;
import wjhj.orbital.sportsmatchfindingapp.utils.DataBindingAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialRequestsFragment extends Fragment {
    private static final String CURR_USER_UID_TAG = "curr_user_uid";

    private SportalRepo repo;
    private String currUserUid;


    public SocialRequestsFragment() {
        // Required empty public constructor
    }

    public static SocialRequestsFragment newInstance(String currUserUid) {
        SocialRequestsFragment fragment = new SocialRequestsFragment();
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
        repo = SportalRepo.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.social_requests_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.social_requests_recycler_view);
        TextView textView = view.findViewById(R.id.social_requests_no_requests_text);

        initRecyclerView(recyclerView, textView);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, TextView textView) {
        LinearLayoutManager manager = new LinearLayoutManager(requireActivity());
        FriendRequestAdapter adapter = new FriendRequestAdapter(new UserProfileItemCallback(), this);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        LiveData<List<String>> friendRequestUids = Transformations.map(repo.getUser(currUserUid),
                UserProfile::getReceivedFriendRequests);

        BatchTransformations.switchMapList(friendRequestUids, repo::getUser, UserProfile::getUid)
                .observe(getViewLifecycleOwner(), userProfiles -> {
                    adapter.submitList(userProfiles);
                    if (userProfiles.isEmpty()) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                });

    }

    public void acceptFriendRequest(String senderUid) {
        repo.acceptFriendRequest(senderUid, currUserUid);
    }

    public void declineFriendRequest(String senderUid) {
        repo.declineFriendRequest(senderUid, currUserUid);
    }

    public void openProfile(String profileUid) {
        DisplayUserProfileFragment fragment = DisplayUserProfileFragment.newInstance(profileUid);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homepage_secondary_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    static class FriendRequestAdapter extends DataBindingAdapter<UserProfile> {

        FriendRequestAdapter(@NonNull DiffUtil.ItemCallback<UserProfile> diffCallback, SocialRequestsFragment controller) {
            super(diffCallback, controller);
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.friend_request_item;
        }
    }
}
