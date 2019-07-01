package wjhj.orbital.sportsmatchfindingapp.game;

public enum Difficulty {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private String str;

    Difficulty(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}