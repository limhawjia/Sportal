package wjhj.orbital.sportsmatchfindingapp.repo;

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

    public GameSearchFilter() {
        this.sportQuery = new ArrayList<>();
        this.timeOfDayQuery = new ArrayList<>();
        this.skillLevelQuery = new ArrayList<>();
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


    // Sir most of these hasSomething() methods always return true cuz u initialized the list in the constructor :)
    public boolean hasSportQuery() {
        return sportQuery != null;
    }

    public boolean hasTimeOfDayQuery() {
        return timeOfDayQuery != null;
    }

    public boolean hasNameQuery() {
        return nameQuery != null;
    }

    public boolean hasSkillLevelQuery() {
        return skillLevelQuery != null;
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
}
