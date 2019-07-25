package wjhj.orbital.sportsmatchfindingapp.user;


import androidx.annotation.NonNull;

import wjhj.orbital.sportsmatchfindingapp.R;

public enum Gender {
    MALE("Male", R.drawable.ic_male_icon),
    FEMALE("Female", R.drawable.ic_female_icon);

    private String str;
    private int drawableResourceId;

    Gender(String str, int drawableResourceId) {
        this.str = str;
        this.drawableResourceId = drawableResourceId;
    }

    public int getDrawableResourceId() {
        return drawableResourceId;
    }

    @NonNull
    @Override
    public String toString() {
        return str;
    }
}