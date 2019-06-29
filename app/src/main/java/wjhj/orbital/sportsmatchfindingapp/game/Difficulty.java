package wjhj.orbital.sportsmatchfindingapp.game;

public enum Difficulty {
    BEGINNER("beginner"),
    INTERMEDIATE("intermediate"),
    ADVANCED("advanced");

    private String str;

    Difficulty(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}