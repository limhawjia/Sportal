package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.SocialChatsFragmentBinding;
import wjhj.orbital.sportsmatchfindingapp.messaging.PrivateChat;
import wjhj.orbital.sportsmatchfindingapp.utils.DataBindingAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialChatsFragment extends Fragment implements DataBindingAdapter.ItemClickListener<PrivateChat> {
    private static final String CURR_USER_UID_TAG = "curr_user_uid";

    private SocialChatsViewModel viewModel;
    private SocialChatsFragmentBinding binding;
    private String mCurrUserUid;

    public SocialChatsFragment() {
        // Required empty public constructor
    }

    public static SocialChatsFragment newInstance(String currUserUid) {
        SocialChatsFragment fragment = new SocialChatsFragment();
        Bundle args = new Bundle();
        args.putString(CURR_USER_UID_TAG, currUserUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCurrUserUid = getArguments().getString(CURR_USER_UID_TAG);
        }

        SocialViewModelFactory factory = new SocialViewModelFactory(mCurrUserUid);
        viewModel = ViewModelProviders.of(this, factory).get(SocialChatsViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = SocialChatsFragmentBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initRecyclerView(binding.socialChatsChannelsList);

        return binding.getRoot();
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        FriendChatChannelAdapter adapter = new FriendChatChannelAdapter(new GroupChannelItemCallback());

        adapter.setLifecycleOwner(getViewLifecycleOwner());
        adapter.setItemClickListener(this);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);



        viewModel.getPrivateChats().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onItemClick(View view, PrivateChat item) {
        if (view.getId() == R.id.chat_card_container) {
            Intent openChatPageIntent = new Intent(requireActivity(), ChatPageActivity.class);
            openChatPageIntent.putExtra(ChatPageActivity.CHANNEL_URL_TAG, item.getChannel().getUrl());
            openChatPageIntent.putExtra(ChatPageActivity.CURR_USER_UID_TAG, mCurrUserUid);
            requireActivity().startActivity(openChatPageIntent);
        }
    }


    static class FriendChatChannelAdapter extends DataBindingAdapter<PrivateChat> {

        FriendChatChannelAdapter(@NonNull DiffUtil.ItemCallback<PrivateChat> diffCallback) {
            super(diffCallback);
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.chat_card_fragment;
        }
    }


    static class GroupChannelItemCallback extends DiffUtil.ItemCallback<PrivateChat> {
        @Override
        public boolean areItemsTheSame(@NonNull PrivateChat oldItem, @NonNull PrivateChat newItem) {
            return oldItem.getChannel().getUrl().equals(newItem.getChannel().getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PrivateChat oldItem, @NonNull PrivateChat newItem) {
            return oldItem.getChannel().getLastMessage().getMessageId()
                    == newItem.getChannel().getLastMessage().getMessageId();
        }
    }

}
