package wjhj.orbital.sportsmatchfindingapp.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.game.TimeOfDay;

public class GameSearchFilter {
    private Sport sportQuery;
    private TimeOfDay timeOfDayQuery;
    private String nameQuery;
    private Difficulty skillLevelQuery;

    private GameSearchFilter() {}

    public GameSearchFilter get() {
        return new GameSearchFilter();
    }

    public void addSportQuery(Sport sport) {
        sportQuery = sport;
    }

    public void addTimeOfDayQuery(TimeOfDay timeOfDay) {
        timeOfDayQuery = timeOfDay;
    }

    public void addNameQuery(String name) {
        nameQuery = name;
    }

    public void addSkillLevelQuery(Difficulty skill) {
        skillLevelQuery = skill;
    }

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

    public Sport getSportQuery() {
        return sportQuery;
    }

    public TimeOfDay getTimeOfDayQuery() {
        return timeOfDayQuery;
    }

    public String getNameQuery() {
        return nameQuery;
    }

    public Difficulty getSkillLevelQuery() {
        return skillLevelQuery;
    }
}
