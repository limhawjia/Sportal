package wjhj.orbital.sportsmatchfindingapp.game;

import com.mapbox.geojson.Point;

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

    public abstract Point getLocation();

    public abstract String getPlaceName();

    public abstract int getMinPlayers();

    public abstract int getMaxPlayers();

    public abstract Difficulty getSkillLevel();

    public abstract LocalDateTime getStartTime();

    public abstract LocalDateTime getEndTime();

    public abstract String getUid();

    public abstract String getCreatorUid();

    public abstract List<String> getParticipatingUids();

    public String dateString() {
        LocalDate startDay = getStartTime().toLocalDate();
        LocalDate endDay = getStartTime().toLocalDate();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (startDay.equals(endDay)) {
            return "Date: " + startDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + "\nFrom: " + getStartTime().format(timeFormatter)
                    + "    To: " + getEndTime().format(timeFormatter);
        } else {
            return "Date: " + startDay.format(DateTimeFormatter.ISO_LOCAL_DATE) + " - "
                    + endDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + "\nFrom: " + getStartTime().format(timeFormatter)
                    + "    To: " + getEndTime().format(timeFormatter);
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