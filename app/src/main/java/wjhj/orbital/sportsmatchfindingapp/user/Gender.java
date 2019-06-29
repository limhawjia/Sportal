package wjhj.orbital.sportsmatchfindingapp.user;

public enum Gender {
    MALE("male"),
    FEMALE("female");

    private String str;

    Gender(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}