package wjhj.orbital.sportsmatchfindingapp.game;

public enum GameStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    COMPLETED("completed");

    private String str;

    GameStatus(String str) {
        this.str = str;
    }

    public static GameStatus fromString(String str) {
        for (GameStatus s : GameStatus.values()) {
            if (s.str.equals(str)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid game status : " + str);
    }

    @Override
    public String toString() {
        return str;
    }
}
