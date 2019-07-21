package wjhj.orbital.sportsmatchfindingapp.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.maps.Country;
import wjhj.orbital.sportsmatchfindingapp.user.Gender;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

class UserProfileDataModel {
    private String displayName;
    private Gender gender;
    private String birthday;
    private Country country;
    private String uid;
    private String bio;
    private String displayPicUri;
    private List<Sport> preferences;
    private Map<String, List<String>> games;

    // Mandatory no args constructor
    public UserProfileDataModel() {
    }

    UserProfileDataModel(UserProfile userProfile) {
        displayName = userProfile.getDisplayName();
        gender = userProfile.getGender();
        birthday = userProfile.getBirthday().toString();
        country = userProfile.getCountry();
        uid = userProfile.getUid();
        bio = userProfile.getBio().orNull();
        displayPicUri = userProfile.getDisplayPicUri().toString();
        preferences = userProfile.getPreferences();
        games = convertGames(userProfile.getGames());

    }

    public String getDisplayName() {
        return displayName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public Country getCountry() {
        return country;
    }

    public String getUid() {
        return uid;
    }

    public String getBio() {
        return bio;
    }

    public String getDisplayPicUri() {
        return displayPicUri;
    }

    public List<Sport> getPreferences() {
        return preferences;
    }

    public Map<String, List<String>> getGames() {
        return games;
    }

    private Map<String, List<String>> convertGames(Map<GameStatus, List<String>> oldGames) {
        Map<String, List<String>> newGames = new HashMap<>();
        for (GameStatus gs : oldGames.keySet()) {
            List<String> games = oldGames.get(gs);
            if (games != null) {
                newGames.put(gs.toString(), games);
            }
        }
        return newGames;
    }
}
