package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Game;

public class GamesDiffCallback extends DiffUtil.Callback {

    private List<Game> oldGames;
    private List<Game> newGames;

    public GamesDiffCallback(List<Game> oldGames, List<Game> newGames) {
        this.oldGames = oldGames;
        this.newGames = newGames;
    }

    @Override
    public int getOldListSize() {
        return oldGames.size();
    }

    @Override
    public int getNewListSize() {
        return newGames.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldGames.get(oldItemPosition).getUid().equals(newGames.get(newItemPosition).getUid());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldGames.get(oldItemPosition).equals(newGames.get(newItemPosition));
    }
}
