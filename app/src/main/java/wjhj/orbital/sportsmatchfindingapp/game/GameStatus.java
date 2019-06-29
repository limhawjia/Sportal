package wjhj.orbital.sportsmatchfindingapp.game;

public enum GameStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    COMPLETED("completed");

    private String id;

    GameStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public static GameStatus fromId(String id) {
        for (GameStatus s : GameStatus.values()) {
            if (s.id.equals(id)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown game status id: " + id);
    }
}
