package wjhj.orbital.sportsmatchfindingapp.game;

import wjhj.orbital.sportsmatchfindingapp.R;

public enum Sport {
    FOOTBALL("Football"),
    BASKETBALL("Basketball"),
    CRICKET("Cricket"),
    BADMINTON("Badminton"),
    TENNIS("Tennis"),
    SQUASH("Squash"),
    FRISBEE("Frisbee");

    private String str;

    Sport(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public int getIconResourceId() {
        switch (str) {
            case "Football":
                return R.drawable.soccer_icon;
            case "Basketball":
                return R.drawable.basketball_icon;
            case "Cricket":
                return R.drawable.cricket_icon;
            case "Badminton":
                return R.drawable.badminton_icon;
            case "Tennis":
                return R.drawable.squash_icon;
            case "Squash":
                return R.drawable.squash_icon;
            case "Frisbee":
                return R.drawable.frisbee_icon;
        }
        return R.drawable.close_button;
    }

    public static String[] getAllSportsString() {
        Sport[] sports = Sport.class.getEnumConstants();
        String[] string = new String[sports.length];
        for (int i = 0; i < sports.length; i++) {
            string[i] = sports[i].toString();
        }
        return string;
    }

    public static Sport[] getAllSports() {
        Sport[] sports = Sport.class.getEnumConstants();
        return sports;
    }
}
