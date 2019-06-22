package wjhj.orbital.sportsmatchfindingapp.user;

import android.util.Log;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;

public class UserProfile {
    public static final String USER_DEBUG = "user";

    private String uid;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private Locale residingCountry;
    private List<String> preferences;
    private Map<GameStatus, List<String>> games;

    //Compulsory public no-argument constructor
    public UserProfile() {
        Log.w(USER_DEBUG, "SHOULD NOT BE SEEING THIS MESSAGE");
    }

    private UserProfile(UserProfile.Builder builder) {
        uid = builder.uid;
        firstName = builder.firstName;
        lastName = builder.lastName;
        gender = builder.gender;
        birthday = builder.birthday;
        residingCountry = builder.residingCountry;
        preferences = builder.preferences;
        games = builder.games;

    }

    public static InitialBuilder builder() {
        return new UserProfile.Builder();
    }

    //Compulsory public getters for each field
    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Locale getResidingCountry() {
        return residingCountry;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public Map<GameStatus, List<String>> getGames() {
        return games;
    }

    //Builder pattern for creating new user instances ourselves
    public static class Builder implements
            InitialBuilder, WithFirstNameBuilder, WithLastNameBuilder, WithGenderBuilder,
            BirthdayOnBuilder, AddOptionalsBuilder {
        private String uid;
        private String firstName;
        private String lastName ;
        private Gender gender;
        private LocalDate birthday;
        private List<String> preferences;
        private Locale residingCountry;
        private Map<GameStatus, List<String>> games;

        private Builder() {
            preferences = new ArrayList<>();
            games = new EnumMap<>(GameStatus.class);
            for (GameStatus status : GameStatus.values()) {
                games.put(status, new ArrayList<>());
            }
        }

        @Override
        public WithFirstNameBuilder ofUid(String uid) {
            this.uid = uid;
            return this;
        }

        @Override
        public WithLastNameBuilder withFirstName(String firstName) {
            if (firstName == null || firstName.isEmpty()) {
                throw new IllegalArgumentException("Please fill in first name!");
            }
            this.firstName = firstName;
            return this;
        }

        @Override
        public WithGenderBuilder withLastName(String lastName) {
            if (lastName == null || lastName.isEmpty()) {
                throw new IllegalArgumentException("Please fill in last name!");
            }
            this.lastName = lastName;
            return this;
        }

        @Override
        public BirthdayOnBuilder withGender(Gender gender) {
            if (gender == null) {
                throw new IllegalArgumentException("Please select a gender");
            }
            this.gender = gender;
            return this;
        }

        @Override
        public AddOptionalsBuilder birthdayOn(LocalDate birthday) {
            if (birthday == null) {
                throw new IllegalArgumentException("Please enter a birthday");
            }
            this.birthday = birthday;
            return this;
        }

        @Override
        public AddOptionalsBuilder addPreference(String preference) {
            this.preferences.add(preference);
            return this;
        }

        @Override
        public AddOptionalsBuilder addAllPreferences(String... preferences) {
            this.preferences.addAll(Arrays.asList(preferences));
            return this;
        }

        @Override
        public AddOptionalsBuilder livesInCountry(Locale residingCountry) {
            this.residingCountry = residingCountry;
            return this;
        }

        @Override
        public UserProfile build() {
            return new UserProfile(this);
        }
    }

    public interface InitialBuilder {
        WithFirstNameBuilder ofUid(String uid);
    }

    public interface WithFirstNameBuilder {
        WithLastNameBuilder withFirstName(String firstName);
    }

    public interface WithLastNameBuilder {
        WithGenderBuilder withLastName(String lastName);
    }

    public interface WithGenderBuilder {
        BirthdayOnBuilder withGender(Gender gender);
    }

    public interface BirthdayOnBuilder {
        UserProfile.AddOptionalsBuilder birthdayOn(LocalDate birthday);
    }

    public interface AddOptionalsBuilder {
        AddOptionalsBuilder addPreference(String preference);
        AddOptionalsBuilder addAllPreferences(String... preferences);
        AddOptionalsBuilder livesInCountry(Locale residingCountry);
        UserProfile build();
    }
}


