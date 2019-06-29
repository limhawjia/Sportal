package wjhj.orbital.sportsmatchfindingapp.game;

import android.location.Location;

import org.immutables.value.Value;
import org.threeten.bp.LocalDateTime;

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
}