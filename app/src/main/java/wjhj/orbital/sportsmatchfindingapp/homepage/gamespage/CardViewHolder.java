package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import wjhj.orbital.sportsmatchfindingapp.databinding.GamesCardViewBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Game;

public class CardViewHolder extends RecyclerView.ViewHolder {
    CardView cardView;
    private GamesCardViewBinding cardBinding;

    CardViewHolder(@NonNull CardView itemView, GamesCardViewBinding binding) {
        super(itemView);
        cardView = itemView;
        cardBinding = binding;
    }

    public void setGame(Game game) {
        cardBinding.setGame(game);
    }
}

