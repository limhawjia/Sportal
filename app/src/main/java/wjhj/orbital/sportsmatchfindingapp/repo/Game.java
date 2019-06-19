package wjhj.orbital.sportsmatchfindingapp.repo;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Game {
    public static final String GAME_DEBUG = "game";

    private String details;
    private String location;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private List<String> usernames;
    private Difficulty skillLevel;
    private String sport;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //Compulsory public no-argument constructor
    public Game() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
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
        private String location;
        private String name;
        private int minPlayers;
        private int maxPlayers;
        private List<String> usernames;
        private Difficulty skillLevel;
        private String sport;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        private Builder() {
            usernames = new ArrayList<>();
            skillLevel = Difficulty.INTERMEDIATE;
        }

        public Builder addDetails(String details) {
            this.details = details;
            return this;
        }
        public Builder addLocation(String location) {
            this.location = location;
            return this;
        }
        public Builder addName(String name) {
            this.name = name;
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
        public Builder addUserName(String... usernames) {
            List<String> existingUsers;
            final String[] input = usernames;

            // REFACTOR THIS!!!!! GAME CLASS SHOULD NOT HAVE A REFERENCE TO DATABASE
            FirebaseFirestore.getInstance()
                    .collection("Admin")
                    .document("Usernames")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d(GAME_DEBUG, "Verified users");
                        Map<String, Object> registeredUsers = documentSnapshot.getData();
                        for (String user : input) {
                            if (!registeredUsers.containsKey(user)) {
                                throw new IllegalArgumentException("User not registered: " + user);
                            }
                        }
                    }).addOnFailureListener(e -> {
                        Log.d(GAME_DEBUG, "Failed to verify users");
                        throw new RuntimeException("Failed to verify users");
                    });
            this.usernames.addAll(Arrays.asList(usernames));
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
            Game game = new Game();
            game.setDetails(this.details);
            game.setLocation(this.location);
            game.setName(this.name);
            game.setSkill(this.skillLevel);
            game.setSport(this.sport);
            game.setMinPlayers(this.minPlayers);
            game.setMaxPlayers(this.maxPlayers);
            game.setUsernames(this.usernames);
            game.setStartTime(this.startTime);
            game.setEndTime(this.endTime);
            // Weird.. refactor
            if (game.location == null || game.name == null || game.sport == null || game.minPlayers == 0) {
                Log.d(GAME_DEBUG, "Unable to create game due to missing fields.");
                throw new RuntimeException("Unable to create game due to missing fields");
            }

            return game;
        }
    }

    public enum Difficulty {
        BEGINNER("Beginner"),
        INTERMEDIATE("Intermediate"),
        ADVANCED("Advanced");

        private String str;

        private Difficulty(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }
}
