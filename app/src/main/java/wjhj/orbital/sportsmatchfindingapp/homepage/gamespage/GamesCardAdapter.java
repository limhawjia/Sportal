package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.databinding.GamesCardViewBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Game;

public class GamesCardAdapter extends RecyclerView.Adapter<GamesCardAdapter.CardViewHolder> {

    public interface GameCardClickedListener {
        void onGameSelected(Game game);
    }

    private List<Game> games;
    private GameCardClickedListener listener;

    public GamesCardAdapter(GameCardClickedListener listener) {
        this.games = new ArrayList<>();
        this.listener = listener;
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
    public void onBindViewHolder(@NonNull GamesCardAdapter.CardViewHolder holder, int position) {
        holder.setGame(games.get(position));
        holder.itemView.setOnClickListener(view -> listener.onGameSelected(games.get(position)));
    }

    @Override
    public int getItemCount() {
        return games.size();
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
            cardBinding.executePendingBindings();
        }
    }
}
