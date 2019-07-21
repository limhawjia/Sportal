package wjhj.orbital.sportsmatchfindingapp.user;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class SportPreferenceDiffCallback extends DiffUtil.Callback {

    private List<Sport> oldList;
    private List<Sport> newList;

    public SportPreferenceDiffCallback(List<Sport> oldList, List<Sport> newList) {
        this.oldList = new ArrayList<>(oldList);
        this.newList = new ArrayList<>(newList);
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
