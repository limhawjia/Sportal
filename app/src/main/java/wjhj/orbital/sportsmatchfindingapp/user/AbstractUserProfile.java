package wjhj.orbital.sportsmatchfindingapp.user;

import android.net.Uri;

import com.google.common.base.Optional;

import org.immutables.value.Value;
import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.Map;

import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.maps.Country;

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

    public abstract Country getCountry();

    public abstract Uri getDisplayPicUri();

    public abstract String getUid();

    public abstract Optional<String> getBio();

    public abstract List<Sport> getPreferences();

    public abstract Map<GameStatus, List<String>> getGames();
}