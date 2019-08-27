package wjhj.orbital.sportsmatchfindingapp.game;

import com.google.common.base.Optional;
import com.google.firebase.firestore.GeoPoint;

import org.immutables.value.Value;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
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

    public abstract Sport getSport();

    public abstract GeoPoint getLocation();

    public abstract String getPlaceName();

    public abstract int getMinPlayers();

    public abstract int getMaxPlayers();

    public abstract Difficulty getSkillLevel();

    public abstract LocalDate getDate();

    public abstract LocalTime getTime();

    public abstract Duration getDuration();

    public abstract String getUid();

    public abstract String getCreatorUid();

    public abstract Optional<String> getDescription();

    public abstract Optional<String> getGameBoardChannelUrl();

    public abstract List<String> getParticipatingUids();

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(getDate(), getTime());
    }

    public String dateString() {
        LocalDate startDay = getDate();
        LocalDate endDay = getStartDateTime().plus(getDuration()).toLocalDate();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (startDay.equals(endDay)) {
            return "Date: " + startDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + "\nFrom: " + getTime().format(timeFormatter)
                    + "    To: " + getTime().plus(getDuration()).format(timeFormatter);
        } else {
            return "Date: " + startDay.format(DateTimeFormatter.ISO_LOCAL_DATE) + " - "
                    + endDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + "\nFrom: " + getTime().format(timeFormatter)
                    + "    To: " + getTime().plus(getDuration()).format(timeFormatter);
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

    private boolean hasEnoughPlayers() {
        return numExtraPlayersNeeded() <= 0;
    }

    public int numExtraPlayersNeeded() {
        int playersNeeded = getMinPlayers() - getParticipatingUids().size();
        return playersNeeded < 0 ? 0 : playersNeeded;
    }

    public boolean isComplete() {
        return getStartDateTime().plus(getDuration())
                .isBefore(LocalDateTime.now());
    }

    private boolean hasMaxPlayers() {
        return getParticipatingUids().size() >= getMaxPlayers();
    }

}