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
    private String placeName;
    private int minPlayers;
    private int maxPlayers;
    private Difficulty skillLevel;
    private String startTime;
    private String endTime;
    private String uid;
    private String creatorUid;
    private List<String> participatingUids;

    // Mandatory no args constructor
    public GameDataModel() {
    }

    GameDataModel(Game game) {
        gameName = game.getGameName();
        description = game.getDescription();
        sport = game.getSport();
        location = new GeoPoint(game.getLocation().latitude(), game.getLocation().longitude());
        placeName = game.getPlaceName();
        minPlayers = game.getMinPlayers();
        maxPlayers = game.getMaxPlayers();
        skillLevel = game.getSkillLevel();
        startTime = game.getStartTime().toString();
        endTime = game.getEndTime().toString();
        uid = game.getUid();
        creatorUid = game.getCreatorUid();
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

    public String getPlaceName() {
        return placeName;
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

    public String getCreatorUid() {
        return creatorUid;
    }

    public List<String> getParticipatingUids() {
        return participatingUids;
    }


}

