package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;


public class AddGameViewModel extends ViewModel {

    private ObservableInt sportSelection = new ObservableInt();
    private MutableLiveData<LocalDate> date = new MutableLiveData<>();
    private MutableLiveData<LocalTime> time = new MutableLiveData<>();
    private MutableLiveData<Duration> duration = new MutableLiveData<>();

    public ObservableInt getSportSelection() {
        return sportSelection;
    }

    public LiveData<LocalDate> date() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.setValue(date);
    }

    public MutableLiveData<LocalTime> getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time.setValue(time);
    }

    public MutableLiveData<Duration> getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration.setValue(duration);
    }


}
