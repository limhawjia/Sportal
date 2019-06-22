package wjhj.orbital.sportsmatchfindingapp.game;

import android.location.Location;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    public static final String GAME_DEBUG = "game";

    private String details;
    private Location location;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private List<String> participating;
    private Difficulty skillLevel;
    private String sport;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //Compulsory public no-argument constructor
    public Game() {
    }

    private Game(Game.Builder builder) {
        details = builder.details;
        location = builder.location;
        name = builder.gameName;
        minPlayers = builder.minPlayers;
        maxPlayers = builder.maxPlayers;
        participating = builder.participating;
        skillLevel = builder.skillLevel;
        sport = builder.sport;
        startTime = builder.startTime;
        endTime = builder.endTime;
    }

    public static Game.Builder builder() {
        return new Game.Builder();
    }

    //Compulsory public getters and setters for each field
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Difficulty getSkill() {
        return skillLevel;
    }

    public void setSkill(Difficulty skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(Integer minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public List<String> getParticipating() {
        return participating;
    }

    public void setUsernames(List<String> usernames) {
        this.participating = usernames;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    //Builder pattern for creating new game instances ourselves
    public static class Builder {
        private String details;
        private Location location;
        private String gameName;
        private int minPlayers;
        private int maxPlayers;
        private List<String> participating;
        private Difficulty skillLevel;
        private String sport;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        private Builder() {
            participating = new ArrayList<>();
            skillLevel = Difficulty.INTERMEDIATE;
        }

        public Builder addDetails(String details) {
            this.details = details;
            return this;
        }
        public Builder addLocation(Location location) {
            this.location = location;
            return this;
        }
        public Builder addName(String name) {
            this.gameName = name;
            return this;
        }
        public Builder setMinPlayers(Integer minPlayers) {
            this.minPlayers = minPlayers;
            return this;
        }
        public Builder setMaxPlayers(Integer maxPlayers) {
            this.maxPlayers = maxPlayers;
            return this;
        }
        public Builder addParticipating(String... participating) {
            this.participating.addAll(Arrays.asList(participating));
            return this;
        }

        public Builder setSkill(Difficulty skillLevel) {
            this.skillLevel = skillLevel;
            return this;
        }

        public Builder setSport(String sport) {
            this.sport = sport;
            return this;
        }

        public Builder setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }

}
