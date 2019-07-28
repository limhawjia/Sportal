package wjhj.orbital.sportsmatchfindingapp.user;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;

public class FriendProfilesAdapter extends RecyclerView.Adapter<FriendProfilesAdapter.ProfileIconViewHolder> {

    private List<UserProfile> friends;
    private UserProfileClickListener listener;

    public FriendProfilesAdapter(UserProfileClickListener listener) {
        this.friends = new ArrayList<>();
        this.listener = listener;
    }

    public void updateFriends(List<UserProfile> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfileIconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ProfileIconViewHolder(inflater.inflate(R.layout.user_profile_icon, parent, false),
                listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileIconViewHolder holder, int position) {
        UserProfile friendProfile = friends.get(position);
        holder.setUserProfile(friendProfile.getDisplayPicUri(), friendProfile.getDisplayName(),
                friendProfile.getUid());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class ProfileIconViewHolder extends RecyclerView.ViewHolder {
        ImageView displayPicView;
        TextView nameView;
        UserProfileClickListener listener;

        ProfileIconViewHolder(@NonNull View itemView, UserProfileClickListener listener) {
            super(itemView);
            this.displayPicView = itemView.findViewById(R.id.user_profile_icon_image);
            this.nameView = itemView.findViewById(R.id.user_profile_icon_name);
            this.listener = listener;
        }

        void setUserProfile(Uri displayPicUri, String name, String uid) {
            Glide.with(displayPicView)
                    .load(displayPicUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(displayPicView);
            nameView.setText(name);
            itemView.setOnClickListener(v -> listener.onUserProfileClick(uid));
        }
    }

    public interface UserProfileClickListener {
        void onUserProfileClick(String uid);
    }
}
