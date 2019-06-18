package wjhj.orbital.sportsmatchfindingapp.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Game {
    public static final String GAME_DATE_FORMAT = "EEE dd-MM-YYYY HH:mm";
    public static final String GAME_DEBUG = "game";

    private String details;
    private String location;
    private String name;
    private Integer minPlayers;
    private Integer maxPlayers;
    private List<String> usernames;
    private String skill;
    private String sport;
    private String startTime;
    private String endTime;

    //Builder pattern for creating new game instances ourselves
    public static class Builder {
        private String details = "";
        private String location = null;
        private String name = null;
        private Integer minPlayers = 0;
        private Integer maxPlayers = 0;
        private List<String> usernames = new ArrayList<>();
        private String skill = "Intermediate";
        private String sport = null;
        private String startTime = "Mon 01-01-0001 00:00";
        private String endTime = "Mon 01-01-0001 00:00";

        private Builder(){}
        public static Builder startBuilder() {
            return new Builder();
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
            Data.db.collection("Admin").document("Usernames").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(GAME_DEBUG, "Verified users");
                    Map<String, Object> registeredUsers = documentSnapshot.getData();
                    for (String user : input) {
                        if (!registeredUsers.containsKey(user)) {
                            throw new IllegalArgumentException("User not registered: " + user);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(GAME_DEBUG, "Failed to verify users");
                    throw new RuntimeException("Failed to verify users");
                }
            });
            this.usernames.addAll(Arrays.asList(usernames));
            return this;
        }
        public Builder setSkill(String skill) {
            if (skill != "beginner" && skill != "intermediate" && skill != "advanced") {
                throw new IllegalArgumentException("Enter a valid skill level: beginner, intermediate, advanced.");
            }
            this.skill = skill;
            return this;
        }
        public Builder setSport(String sport) {
            this.sport = sport;
            return this;
        }
        public Builder setStartTime(Date startTime) {
            this.startTime = new SimpleDateFormat(GAME_DATE_FORMAT).format(startTime);
            return this;
        }
        public Builder setEndTime(Date endTime) {
            this.startTime = new SimpleDateFormat(GAME_DATE_FORMAT).format(endTime);
            return this;
        }

        public Game build() {
            Game game = new Game();
            game.setDetails(this.details);
            game.setLocation(this.location);
            game.setName(this.name);
            game.setSkill(this.skill);
            game.setSport(this.sport);
            game.setMinPlayers(this.minPlayers);
            game.setMaxPlayers(this.maxPlayers);
            game.setUsernames(this.usernames);
            game.setStartTime(this.startTime);
            game.setEndTime(this.endTime);

            if (game.location == null || game.name == null || game.sport == null || game.minPlayers == 0) {
                Log.d(GAME_DEBUG, "Unable to create game due to missing fields.");
                throw new RuntimeException("Unable to create game due to missing fields");
            }

            return game;
        }
    }

    //Compulsory public no-argument constructor
    public Game() {
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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Integer getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(Integer minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Integer getMaxPlayers() {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
