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

    public abstract String getUid();

    public abstract Optional<String> getBio();

    @Value.Default
    public Uri getDisplayPicUri() {
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/orbital2019-8cd87.appspot.com/o/display-images%2Fdefault_display_pic.png?alt=media&token=7275d878-8ccc-4e46-9c37-3d26ec533a05");
    }

    public abstract List<Sport> getPreferences();

    public abstract Map<GameStatus, List<String>> getGames();
}