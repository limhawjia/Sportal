package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.databinding.FriendsCardViewBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.user.PreferencesIconAdapter;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public class FriendsCardAdapter extends ListAdapter<UserProfile, FriendsCardAdapter.CardViewHolder> {

    interface FriendsCardListener {
        void onCardClicked(String profileUid);
    }

    private FriendsCardListener listener;
    private Task<List<Sport>> currUserPreferencesTask;

    FriendsCardAdapter(FriendsCardListener listener, Task<List<Sport>> currUserPreferencesTask) {
        super(new UserProfileItemCallback());
        this.listener = listener;
        this.currUserPreferencesTask = currUserPreferencesTask;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FriendsCardViewBinding binding = FriendsCardViewBinding.inflate(inflater, parent, false);
        return new CardViewHolder(binding, listener, currUserPreferencesTask);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        private FriendsCardViewBinding binding;
        private FriendsCardListener listener;
        private Task<List<Sport>> currUserPreferencesTask;

        CardViewHolder(@NonNull FriendsCardViewBinding binding, FriendsCardListener listener,
                       Task<List<Sport>> currUserPreferencesTask) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
            this.currUserPreferencesTask = currUserPreferencesTask;
        }

        void bind(UserProfile item) {
            binding.setItem(item);
            binding.getRoot().setOnClickListener(v -> listener.onCardClicked(item.getUid()));

            LinearLayoutManager manager = new LinearLayoutManager(binding.getRoot().getContext(),
                    RecyclerView.HORIZONTAL, false);
            PreferencesIconAdapter adapter = new PreferencesIconAdapter();
            binding.friendsCardCommonPreferencesList.setLayoutManager(manager);
            binding.friendsCardCommonPreferencesList.setAdapter(adapter);
            binding.friendsCardCommonPreferencesList.setNestedScrollingEnabled(true);

            currUserPreferencesTask.addOnSuccessListener(preferences -> {
                List<Sport> preferencesCopy = new ArrayList<>(preferences);
                preferencesCopy.retainAll(item.getPreferences());
                adapter.updatePreferences(preferencesCopy);
            });
        }
    }
}
