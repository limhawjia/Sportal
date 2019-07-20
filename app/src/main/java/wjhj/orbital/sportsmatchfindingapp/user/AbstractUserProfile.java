package wjhj.orbital.sportsmatchfindingapp.user;

import org.immutables.value.Value;
import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

@Value.Style(
        stagedBuilder = true,
        depluralize = true,
        typeImmutable = "*",
        init = "with*",
        create = "new"
)
@Value.Immutable
public abstract class AbstractUserProfile {

    public abstract String getDisplayName();

    public abstract Gender getGender();

    public abstract LocalDate getBirthday();

    //TODO: Add in Country, Bio AND displayPicURI parameters and refactor shit to match that :)

    public abstract String getUid();

    public abstract List<Sport> getPreferences();

    public abstract Map<GameStatus, List<String>> getGames();
}