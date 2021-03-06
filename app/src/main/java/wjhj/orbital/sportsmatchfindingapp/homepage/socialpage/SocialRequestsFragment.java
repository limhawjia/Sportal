package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.DisplayUserProfileFragment;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;
import wjhj.orbital.sportsmatchfindingapp.utils.BatchTransformations;
import wjhj.orbital.sportsmatchfindingapp.utils.DataBindingListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialRequestsFragment extends Fragment implements DataBindingListAdapter.ItemClickListener<UserProfile> {
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
        FriendRequestAdapter adapter = new FriendRequestAdapter(new UserProfileItemCallback());
        adapter.setItemClickListener(this);

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


    @Override
    public void onItemClick(View view, UserProfile item) {
        switch (view.getId()) {
            case R.id.friend_request_container:
                DisplayUserProfileFragment fragment = DisplayUserProfileFragment.newInstance(item.getUid());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homepage_secondary_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.friend_request_accept_button:
                repo.acceptFriendRequest(item.getUid(), currUserUid);
                break;
            case R.id.friend_request_decline_button:
                repo.declineFriendRequest(item.getUid(), currUserUid);
                break;
        }

    }


    static class FriendRequestAdapter extends DataBindingListAdapter<UserProfile> {

        FriendRequestAdapter(@NonNull DiffUtil.ItemCallback<UserProfile> diffCallback) {
            super(diffCallback);
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.friend_request_item;
        }
    }
}
