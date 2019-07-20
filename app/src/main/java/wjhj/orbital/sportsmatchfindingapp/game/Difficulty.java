package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.annotation.NonNull;

public enum Difficulty {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private String str;

    Difficulty(String str) {
        this.str = str;
    }

    public static String[] getAllDifficultyStrings() {
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