package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.annotation.NonNull;

import wjhj.orbital.sportsmatchfindingapp.R;

public enum Sport {
    FOOTBALL("Football", R.drawable.soccer_icon),
    BASKETBALL("Basketball", R.drawable.basketball_icon),
    CRICKET("Cricket", R.drawable.cricket_icon),
    BADMINTON("Badminton",  R.drawable.badminton_icon),
    TENNIS("Tennis", R.drawable.squash_icon),
    SQUASH("Squash", R.drawable.squash_icon),
    FRISBEE("Frisbee", R.drawable.frisbee_icon);

    private String str;
    private int iconResourceId;

    Sport(String str, int iconResourceId) {
        this.str = str;
        this.iconResourceId = iconResourceId;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public static String[] getAllSportsString() {
        String[] strings = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            strings[i] = values()[i].toString();
        }
        return strings;
    }

    @NonNull
    @Override
    public String toString() {
        return str;
    }
}
