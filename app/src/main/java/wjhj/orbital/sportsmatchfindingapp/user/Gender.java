package wjhj.orbital.sportsmatchfindingapp.user;

import wjhj.orbital.sportsmatchfindingapp.R;

public enum Gender {
    MALE("Male", R.drawable.ic_male_solid),
    FEMALE("Female", R.drawable.ic_female_solid);

    private String str;
    private int iconResourceId;

    Gender(String str, int iconResourceId) {
        this.str = str;
        this.iconResourceId = iconResourceId;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    @Override
    public String toString() {
        return str;
    }

}