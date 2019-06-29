package wjhj.orbital.sportsmatchfindingapp.repo;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Game;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

class GameDataModel {
    private String gameName;
    private String description;
    private Sport sport;
    private GeoPoint location;
    private int minPlayers;
    private int maxPlayers;
    private Difficulty skillLevel;
    private String startTime;
    private String endTime;
    private String uid;
    private List<String> participatingUids;

    // Mandatory no args constructor
    public GameDataModel() {
    }

    GameDataModel(Game game) {
        gameName = game.getGameName();
        description = game.getDescription();
        sport = game.getSport();
        location = new GeoPoint(game.getLocation().getLatitude(), game.getLocation().getLongitude());
        minPlayers = game.getMinPlayers();
        maxPlayers = game.getMaxPlayers();
        skillLevel = game.getSkillLevel();
        startTime = game.getStartTime().toString();
        endTime = game.getEndTime().toString();
        uid = game.getUid();
        participatingUids = game.getParticipatingUids();
    }

    public String getGameName() {
        return gameName;
    }

    public String getDescription() {
        return description;
    }

    public Sport getSport() {
        return sport;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Difficulty getSkillLevel() {
        return skillLevel;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getUid() {
        return uid;
    }

    public List<String> getParticipatingUids() {
        return participatingUids;
    }
}
