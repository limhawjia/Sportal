package wjhj.orbital.sportsmatchfindingapp.game;

public enum GameStatus {
    PENDING("pending", 0),
    CONFIRMED("ready", 1),
    COMPLETED("completed", 2);

    private String str;
    private int id;

    GameStatus(String str, int id) {
        this.str = str;
        this.id = id;
    }

    public static GameStatus fromString(String str) {
        for (GameStatus s : GameStatus.values()) {
            if (s.str.equals(str)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid game status : " + str);
    }

    public static GameStatus fromId(int id) {
        for (GameStatus s : GameStatus.values()) {
            if (s.id == id) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid game id : " + id);
    }

    @Override
    public String toString() {
        return str;
    }
}
