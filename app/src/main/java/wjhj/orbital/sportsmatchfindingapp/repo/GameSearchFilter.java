package wjhj.orbital.sportsmatchfindingapp.repo;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.game.TimeOfDay;

public class GameSearchFilter {
    private List<Sport> sportQuery;
    private List<TimeOfDay> timeOfDayQuery;
    private String nameQuery;
    private List<Difficulty> skillLevelQuery;
    private GeoPoint locationQuery;
    private double locationQueryRadius;

    public GameSearchFilter() {
        this.sportQuery = new ArrayList<>();
        this.timeOfDayQuery = new ArrayList<>();
        this.skillLevelQuery = new ArrayList<>();
        this.locationQueryRadius = 10.0;
    }

    public void addSportQuery(Sport... sports) {
        sportQuery.addAll(Arrays.asList(sports));
    }

    public void setSportQuery(List<Sport> sports) {
        sportQuery = sports;
    }

    public void addTimeOfDayQuery(TimeOfDay... timesOfDay) {
        timeOfDayQuery.addAll(Arrays.asList(timesOfDay));
    }

    public void setTimeOfDayQuery(List<TimeOfDay> timesOfDay) {
        timeOfDayQuery = timesOfDay;
    }

    public void setNameQuery(String name) {
        nameQuery = name;
    }

    public void addSkillLevelQuery(Difficulty... skills) {
        skillLevelQuery.addAll(Arrays.asList(skills));
    }

    public void setSkillLevelQuery(List<Difficulty> skills) {
        skillLevelQuery = skills;
    }

    public List<Sport> getSportQuery() {
        return sportQuery;
    }

    public List<TimeOfDay> getTimeOfDayQuery() {
        return timeOfDayQuery;
    }

    public String getNameQuery() {
        return nameQuery;
    }

    public List<Difficulty> getSkillLevelQuery() {
        return skillLevelQuery;
    }

    public void setLocationQuery(GeoPoint location) {
        this.locationQuery = location;
    }

    public boolean hasLocationQuery() {
        return this.locationQuery != null;
    }

    public GeoPoint getLocationQuery() {
        return this.locationQuery;
    }

    public void setLocationQueryRadius(double radius) {
        this.locationQueryRadius = radius;
    }

    public double getLocationQueryRadius() {
        return this.locationQueryRadius;
    }
}
