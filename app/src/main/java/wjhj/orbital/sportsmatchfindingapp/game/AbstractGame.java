package wjhj.orbital.sportsmatchfindingapp.game;

import android.location.Location;

import org.immutables.value.Value;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

@Value.Style(
        stagedBuilder = true,
        depluralize = true,
        typeImmutable = "*",
        init = "with*",
        create = "new"
)
@Value.Immutable
public abstract class AbstractGame {

    public abstract String getGameName();

    public abstract String getDescription();

    public abstract Sport getSport();

    //TODO: Type to be changed depending on map implementation
    public abstract Location getLocation();

    public abstract int getMinPlayers();

    public abstract int getMaxPlayers();

    public abstract Difficulty getSkillLevel();

    public abstract LocalDateTime getStartTime();

    public abstract LocalDateTime getEndTime();

    public abstract String getUid();

    public abstract List<String> getParticipatingUids();

    // TODO: Maybe implement a precondition check for minimum number of participating.
    // Alternatively, refactor to have a game creator attribute.
    public String dateString() {
        LocalDate startDay = getStartTime().toLocalDate();
        LocalDate endDay = getStartTime().toLocalDate();

        if (startDay.equals(endDay)) {
            return "Date: " + startDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + "\nFrom: " + getStartTime().toLocalTime().toString()
                    + "    To: " + getEndTime().toLocalTime().toString();
        } else {
            return "Date: " + startDay.format(DateTimeFormatter.ISO_LOCAL_DATE) + " - "
                    + endDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + "\nFrom: " + getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME)
                    + "    To: " + getEndTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }

    public String playersNeededMessage() {
        if (hasMaxPlayers()) {
             return getMaxPlayers() + " / " + getMaxPlayers() + " players attending!";
        } else if (hasEnoughPlayers()) {
            return "Enough players have joined!";
        } else if (numExtraPlayersNeeded() == 1) {
            return "1 more player needed!";
        } else {
            return numExtraPlayersNeeded() + " more players needed";
        }
    }

    public boolean hasEnoughPlayers() {
        return numExtraPlayersNeeded() <= 0;
    }

    public int numExtraPlayersNeeded() {
        int playersNeeded = getMinPlayers() - getParticipatingUids().size();
        return playersNeeded < 0 ? 0 : playersNeeded;
    }

    private boolean hasMaxPlayers() {
        return getParticipatingUids().size() >= getMaxPlayers();
    }
}