package wjhj.orbital.sportsmatchfindingapp.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class PreferencesIconAdapter extends RecyclerView.Adapter {

    private List<Sport> sportPreferences;

    public PreferencesIconAdapter() {
        this.sportPreferences = new ArrayList<>();
    }

    public void updatePreferences(List<Sport> preferences) {
        sportPreferences = preferences;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new IconViewHolder(inflater.inflate(R.layout.preferences_icon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((IconViewHolder) holder).setIconDrawable(Sport.values()[position].getIconResourceId());
    }

    @Override
    public int getItemCount() {
        return sportPreferences.size();
    }

    static class IconViewHolder extends RecyclerView.ViewHolder {
        ImageView iconView;

        IconViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.sport_preference_icon);
        }

        void setIconDrawable(int imageResource) {
            iconView.setImageResource(imageResource);
        }

    }
}
