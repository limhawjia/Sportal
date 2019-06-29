package wjhj.orbital.sportsmatchfindingapp.user;

import org.immutables.value.Value;
import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;

@Value.Style(
        stagedBuilder = true,
        depluralize = true,
        typeImmutable = "*",
        init = "with*",
        create = "new"
)
@Value.Immutable
public abstract class AbstractUserProfile {
    //FIND OUT WHAT ENUM CONSTANT WARNING IS: unknown enum constant Modifier.FINAL
    public abstract String getDisplayName();

    public abstract Gender getGender();

    public abstract LocalDate getBirthday();

    public abstract List<String> getPreferences();

    public abstract Map<GameStatus, List<String>> getGames();
}