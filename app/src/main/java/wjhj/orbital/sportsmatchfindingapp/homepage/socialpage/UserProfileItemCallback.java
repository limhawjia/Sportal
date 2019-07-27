package wjhj.orbital.sportsmatchfindingapp.homepage.socialpage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

public class UserProfileItemCallback extends DiffUtil.ItemCallback<UserProfile> {
    @Override
    public boolean areItemsTheSame(@NonNull UserProfile oldItem, @NonNull UserProfile newItem) {
        return oldItem.getUid().equals(newItem.getUid());
    }

    @Override
    public boolean areContentsTheSame(@NonNull UserProfile oldItem, @NonNull UserProfile newItem) {
        return oldItem.equals(newItem);
    }
}
