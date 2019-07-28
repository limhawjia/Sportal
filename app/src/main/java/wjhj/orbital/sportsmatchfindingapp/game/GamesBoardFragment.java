package wjhj.orbital.sportsmatchfindingapp.game;


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
import android.widget.Toast;

import com.sendbird.android.BaseMessage;
import com.sendbird.android.SendBird;
import com.sendbird.android.UserMessage;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.GamesBoardFragmentBinding;
import wjhj.orbital.sportsmatchfindingapp.utils.DataBindingAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesBoardFragment extends Fragment {
    private static final String GAME_UID_TAG = "game_uid";

    private String mGameUid;
    private GamesBoardViewModel viewModel;
    private GamesBoardFragmentBinding binding;


    public GamesBoardFragment() {
        // Required empty public constructor
    }

    public static GamesBoardFragment newInstance(String gameUid) {
        GamesBoardFragment fragment = new GamesBoardFragment();
        Bundle args = new Bundle();
        args.putString(GAME_UID_TAG, gameUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameUid = getArguments().getString(GAME_UID_TAG);
        }
        GameUidViewModelFactory factory = new GameUidViewModelFactory(mGameUid);
        viewModel = ViewModelProviders.of(this, factory).get(GamesBoardViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = GamesBoardFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        initRecyclerView(binding.gamesBoardMessageList);

        viewModel.getConnectionExceptions().observe(this, e ->
                Toast.makeText(requireContext(), "Connection error, please try again.", Toast.LENGTH_LONG)
                        .show());

        return binding.getRoot();
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,
                true);
        manager.setSmoothScrollbarEnabled(true);

        GameBoardMessageAdapter adapter = new GameBoardMessageAdapter(new DiffUtil.ItemCallback<BaseMessage>() {
            @Override
            public boolean areItemsTheSame(@NonNull BaseMessage oldItem, @NonNull BaseMessage newItem) {
                return oldItem.getMessageId() == newItem.getMessageId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull BaseMessage oldItem, @NonNull BaseMessage newItem) {
                return oldItem.equals(newItem);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                manager.scrollToPositionWithOffset(positionStart, 0);
            }
        });
        adapter.setLifecycleOwner(this);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (manager.findLastVisibleItemPosition() == adapter.getItemCount() - 1) {
                    viewModel.loadPreviousMessages();
                }
            }
        });

        viewModel.getMessagesLiveData().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    static class GameBoardMessageAdapter extends DataBindingAdapter<BaseMessage> {

        GameBoardMessageAdapter(@NonNull DiffUtil.ItemCallback<BaseMessage> diffCallback) {
            super(diffCallback);
        }

        @Override
        public int getItemViewType(int position) {
            BaseMessage message = getItem(position);
            if (message instanceof UserMessage) {
                if (((UserMessage) message).getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {
                    Timber.d(((UserMessage) message).getSender().getUserId());
                    return R.layout.game_board_message_self_layout;
                } else {
                    return R.layout.game_board_message_other_layout;
                }
            }
            return R.layout.game_board_message_other_layout; // To be changed if more message types are supported in the future.
        }
    }

}
