package wjhj.orbital.sportsmatchfindingapp.game;

public enum Sport {
    FOOTBALL("football"),
    BASKETBALL("basketball"),
    CRICKET("cricket"),
    BADMINTON("badminton"),
    TENNIS("tennis"),
    SQUASH("squash"),
    FRISBEE("frisbee");

    private String str;

    Sport(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
