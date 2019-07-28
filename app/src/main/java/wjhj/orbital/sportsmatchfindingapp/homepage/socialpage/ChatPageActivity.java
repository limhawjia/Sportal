package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.SendBird;
import com.sendbird.android.UserMessage;

import java.util.List;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.ChatPageActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.messaging.PrivateChat;
import wjhj.orbital.sportsmatchfindingapp.user.DisplayUserProfileFragment;
import wjhj.orbital.sportsmatchfindingapp.utils.DataBindingAdapter;

public class ChatPageActivity extends AppCompatActivity {
    public static final String CURR_USER_UID_TAG = "curr_user_uid";
    public static final String CHANNEL_URL_TAG = "channel_url";

    private ChatPageViewModel viewModel;
    private ChatPageActivityBinding binding;
    private String mCurrUserUid;
    private String mChannelUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrUserUid = getIntent().getStringExtra(CURR_USER_UID_TAG);
        mChannelUrl = getIntent().getStringExtra(CHANNEL_URL_TAG);

        if (mCurrUserUid == null || mChannelUrl == null) {
            throw new IllegalArgumentException("Must supply channel url and curr user uid to intent");
        }

        initViewModel();

        binding = DataBindingUtil.setContentView(this, R.layout.chat_page_activity);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initTopToolbar((Toolbar) binding.topToolbar);

        initRecyclerView(binding.chatPageMessageList);
    }

    private void initViewModel() {
        InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ChatPageViewModelFactory factory = new ChatPageViewModelFactory(mCurrUserUid, mChannelUrl, iMM);
        viewModel = ViewModelProviders.of(this, factory).get(ChatPageViewModel.class);
    }

    private void initTopToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView titleText = toolbar.findViewById(R.id.top_toolbar_title);
        ImageView profileIcon = toolbar.findViewById(R.id.top_toolbar_icon);


        Transformations.switchMap(viewModel.getChatLiveData(), PrivateChat::getFriendDisplayName)
                .observe(this, titleText::setText);

        Transformations.switchMap(viewModel.getChatLiveData(), PrivateChat::getFriendDisplayPicUri)
                .observe(this, uri -> Glide.with(this)
                        .load(uri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profileIcon));


        Transformations.switchMap(viewModel.getChatLiveData(), PrivateChat::getFriendUid)
                .observe(this, uid -> {
                    titleText.setOnClickListener(v -> openProfile(uid));
                    profileIcon.setOnClickListener(v -> openProfile(uid));
                });
    }

    private void openProfile(String friendUid) {
        DisplayUserProfileFragment fragment = DisplayUserProfileFragment.newInstance(friendUid);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.chat_page_activity_secondary_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL,
                true);

        MessageAdapter adapter = new MessageAdapter(new DiffUtil.ItemCallback<BaseMessage>() {
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
                List<BaseMessage> messages = viewModel.getMessagesLiveData().getValue();

                Timber.d("bein scrolled and conditions correct: %d", manager.findFirstVisibleItemPosition());
                if (messages != null && (manager.findFirstVisibleItemPosition() == messages.size() - 1)) {
                    viewModel.loadPreviousMessages();
                }
            }
        });

        viewModel.getMessagesLiveData().observe(this, adapter::submitList);
    }


    static class MessageAdapter extends DataBindingAdapter<BaseMessage> {

        MessageAdapter(@NonNull DiffUtil.ItemCallback<BaseMessage> diffCallback) {
            super(diffCallback);
        }

        @Override
        public int getItemViewType(int position) {
            BaseMessage message = getItem(position);
            if (message instanceof UserMessage) {
                if (((UserMessage) message).getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {
                    return R.layout.user_message_self_layout;
                } else {
                    return R.layout.user_message_other_layout;
                }
            }
            return 0; // To be changed if more message types are supported in the future.
        }
    }
}
