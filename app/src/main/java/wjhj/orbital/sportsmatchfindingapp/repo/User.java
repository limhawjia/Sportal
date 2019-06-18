package wjhj.orbital.sportsmatchfindingapp.repo;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class User {
    public static final String USER_BIRTHDAY_FORMAT = "dd-MM-yyyy";
    public static final String USER_DEBUG = "user";

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String gender;
    private String birthday;
    private String location;
    private List<String> preferences;
    private List<String> pendingGames;
    private List<String> confirmedGames;
    private List<String> completedGames;

    //Builder pattern for creating new user instances ourselves
    public static class Builder {
        private String firstName = null;
        private String middleName = null;
        private String lastName = null;
        private String email = null;
        private String gender = null;
        private String birthday = null;
        private String location = null;
        private List<String> preferences = new ArrayList<>();
        private List<String> pendingGames = new ArrayList<>();
        private List<String> confirmedGames = new ArrayList<>();
        private List<String> completedGames = new ArrayList<>();

        private Builder() {
        }

        public static Builder startBuilder() {
            return new Builder();
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            if (!email.contains("@")) {
                Log.d(USER_DEBUG, "Invalid email.");
                throw new IllegalArgumentException("Invalid email.");
            }
            this.email = email;
            return this;
        }

        public Builder setGender(String gender) {
            if (gender != "male" && gender != "female") {
                Log.d(USER_DEBUG, "Invalid gender.");
                throw new IllegalArgumentException("Invalid gender.");
            }
            this.gender = gender;
            return this;
        }

        public Builder setBirthday(Date birthday) {
            this.birthday = new SimpleDateFormat(USER_BIRTHDAY_FORMAT).format(birthday);
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setPreferences(String... preferences) {
            this.preferences = Arrays.asList(preferences);
            return this;
        }

        public User build() {
            if (this.firstName == null || this.lastName == null || this.email == null
                    || this.birthday == null || this.gender == null || this.location == null
                    || this.preferences.size() < 1) {
                Log.d(USER_DEBUG, "Unable to create game due to missing fields.");
                throw new RuntimeException("Unable to create game due to missing fields");
            }

            User user = new User();
            user.setFirstName(this.firstName);
            user.setMiddleName(this.middleName);
            user.setLastName(this.lastName);
            user.setEmail(this.email);
            user.setGender(this.gender);
            user.setBirthday(this.birthday);
            user.setLocation(this.location);
            user.setPreferences(this.preferences);
            user.setPendingGames(this.pendingGames);
            user.setConfirmedGames(this.confirmedGames);
            user.setCompletedGames(this.completedGames);

            return user;
        }
    }


    //Compulsory public no-argument constructor
    public User() {
    }

    //Compulsory public getters and setters for each field
    public static String getUserBirthdayFormat() {
        return USER_BIRTHDAY_FORMAT;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public List<String> getPendingGames() {
        return pendingGames;
    }

    public void setPendingGames(List<String> pendingGames) {
        this.pendingGames = pendingGames;
    }

    public List<String> getConfirmedGames() {
        return confirmedGames;
    }

    public void setConfirmedGames(List<String> confirmedGames) {
        this.confirmedGames = confirmedGames;
    }

    public List<String> getCompletedGames() {
        return completedGames;
    }

    public void setCompletedGames(List<String> completedGames) {
        this.completedGames = completedGames;
    }
}
