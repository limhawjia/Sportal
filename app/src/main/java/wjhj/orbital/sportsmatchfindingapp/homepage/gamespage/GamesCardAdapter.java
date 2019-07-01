package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.databinding.GamesCardViewBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Game;

public class GamesCardAdapter extends RecyclerView.Adapter<GamesCardAdapter.CardViewHolder> implements Filterable {

    private List<Game> games;

    public GamesCardAdapter() {
        this.games = new ArrayList<>();
    }

    public void updateGames(List<Game> newGames) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new GamesDiffCallback(games, newGames),
                true);
        games = newGames;
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public GamesCardAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view for each data in the set
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        GamesCardViewBinding binding = GamesCardViewBinding.inflate(inflater, parent, false);

        return new CardViewHolder((CardView) binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.setGame(games.get(position));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @BindingAdapter("android:bindingSrc")
    public static void setImageResource(ImageView view, int resource) {
        view.setImageResource(resource);
    }



    static class CardViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        private GamesCardViewBinding cardBinding;

        CardViewHolder(@NonNull CardView itemView, GamesCardViewBinding binding) {
            super(itemView);
            cardView = itemView;
            cardBinding = binding;
        }

        private void setGame(Game game) {
            cardBinding.setGame(game);
        }
    }

}
