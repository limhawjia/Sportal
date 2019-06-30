package wjhj.orbital.sportsmatchfindingapp.homepage;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.databinding.GamesCardViewBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Game;

public class GamesCardAdapter extends RecyclerView.Adapter<GamesCardAdapter.CardViewHolder> {

    private List<Game> games;

    public GamesCardAdapter() {
        this.games = new ArrayList<>();
    }

    public void updateGames(List<Game> newGames) {
        games = newGames;
        notifyDataSetChanged();
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


    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        private GamesCardViewBinding cardBinding;

        public CardViewHolder(@NonNull CardView itemView, GamesCardViewBinding binding) {
            super(itemView);
            cardView = itemView;
            cardBinding = binding;
        }

        private void setGame(Game game) {
            cardBinding.setGame(game);
        }
    }
}
