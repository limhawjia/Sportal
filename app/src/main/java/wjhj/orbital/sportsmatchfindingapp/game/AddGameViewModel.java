package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.mapbox.geojson.Point;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;


public class AddGameViewModel extends ViewModel {

    private ObservableInt sportSelection = new ObservableInt();
    private MutableLiveData<String> gameName = new MutableLiveData<>();
    private MutableLiveData<LocalDate> date = new MutableLiveData<>();
    private MutableLiveData<LocalTime> time = new MutableLiveData<>();
    private MutableLiveData<Duration> duration = new MutableLiveData<>();
    private MutableLiveData<String> minPlayersInput = new MutableLiveData<>();
    private MutableLiveData<String> maxPlayersInput = new MutableLiveData<>();
    private Point locationPoint;
    private MutableLiveData<String> placeName = new MutableLiveData<>();

    public ObservableInt getSportSelection() {
        return sportSelection;
    }

    public MutableLiveData<String> getGameName() {
        return gameName;
    }

    public LiveData<LocalDate> getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.setValue(date);
    }

    public LiveData<LocalTime> getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time.setValue(time);
    }

    public LiveData<Duration> getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration.setValue(duration);
    }

    public LiveData<String> durationString() {
        return Transformations.map(duration, value -> {
            long hours = value.getSeconds() / 3600;
            long minutes = (value.getSeconds() % 3600) / 60;
            return hours + " h  " + minutes + " m";
        });
    }

    public MutableLiveData<String> getMinPlayersInput() {
        return minPlayersInput;
    }

    public MutableLiveData<String> getMaxPlayersInput() {
        return maxPlayersInput;
    }

    public Point getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(Point locationPoint) {
        this.locationPoint = locationPoint;
    }

    public MutableLiveData<String> getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName.setValue(placeName);
    }
}
