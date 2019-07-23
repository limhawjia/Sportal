package wjhj.orbital.sportsmatchfindingapp.user;


import androidx.annotation.NonNull;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private String str;

    Gender(String str) {
        this.str = str;
    }

    @NonNull
    @Override
    public String toString() {
        return str;
    }
}