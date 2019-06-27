package wjhj.orbital.sportsmatchfindingapp.user;


import android.location.Location;

import com.google.common.base.Optional;

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
    //TODO: FIND OUT WHAT ENUM CONSTANT WARNING IS: unknown enum constant Modifier.FINAL
    public abstract String getUid();

    public abstract Gender getGender();

    public abstract LocalDate getBirthday();

    public abstract Optional<Location> getResidingCountry();

    public abstract List<String> getPreferences();

    public abstract Map<GameStatus, List<String>> getGames();
}