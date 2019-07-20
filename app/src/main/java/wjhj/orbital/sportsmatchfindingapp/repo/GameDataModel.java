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

     String getGameName() {
        return gameName;
    }

     String getDescription() {
        return description;
    }

     Sport getSport() {
        return sport;
    }

     GeoPoint getLocation() {
        return location;
    }

     String getPlaceName() {
        return placeName;
    }

     int getMinPlayers() {
        return minPlayers;
    }

     int getMaxPlayers() {
        return maxPlayers;
    }

     Difficulty getSkillLevel() {
        return skillLevel;
    }

     String getStartTime() {
        return startTime;
    }

     String getEndTime() {
        return endTime;
    }

     String getUid() {
        return uid;
    }

     String getCreatorUid() {
        return creatorUid;
    }

     List<String> getParticipatingUids() {
        return participatingUids;
    }


}

