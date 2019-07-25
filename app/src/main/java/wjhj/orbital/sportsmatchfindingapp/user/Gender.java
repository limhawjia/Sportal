package wjhj.orbital.sportsmatchfindingapp.user;


import androidx.annotation.NonNull;

import wjhj.orbital.sportsmatchfindingapp.R;

public enum Gender {
    MALE("Male", 0, R.drawable.ic_male_icon),
    FEMALE("Female", 1, R.drawable.ic_female_icon);

    private String str;
    private int position;
    private int drawableResourceId;

    Gender(String str, int position, int drawableResourceId) {
        this.str = str;
        this.position = position;
        this.drawableResourceId = drawableResourceId;
    }

    public int getPosition() {
        return position;
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