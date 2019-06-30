package wjhj.orbital.sportsmatchfindingapp.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.user.Gender;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

class UserProfileDataModel {
    private String displayName;
    private Gender gender;
    private String birthday;
    private String uid;
    private List<String> preferences;
    private Map<String, List<String>> games;

    // Mandatory no args constructor
    public UserProfileDataModel() {
    }

    UserProfileDataModel(UserProfile userProfile) {
        displayName = userProfile.getDisplayName();
        gender = userProfile.getGender();
        birthday = userProfile.getBirthday().toString();
        uid = userProfile.getUid();
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

    public String getUid() {
        return uid;
    }

    public List<String> getPreferences() {
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
