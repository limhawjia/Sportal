package wjhj.orbital.sportsmatchfindingapp.repo;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.user.Gender;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

class UserProfileDataModel {
    private String uid;
    private Gender gender;
    private String birthday;
    private GeoPoint residingCountry;
    private List<String> preferences;
    private Map<String, List<String>> games;

    // Mandatory no args constructor
    public UserProfileDataModel() {
    }

    UserProfileDataModel(UserProfile userProfile) {
        uid = userProfile.getUid();
        gender = userProfile.getGender();
        birthday = userProfile.getBirthday().toString();
        residingCountry = userProfile.getResidingCountry()
                .transform(loc -> new GeoPoint(loc.getLatitude(), loc.getLongitude()))
                .orNull();
        preferences = userProfile.getPreferences();
        games = convertGames(userProfile.getGames());

    }

    public String getUid() {
        return uid;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public GeoPoint getResidingCountry() {
        return residingCountry;
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
                newGames.put(gs.getId(), games);
            }
        }
        return newGames;
    }
}
