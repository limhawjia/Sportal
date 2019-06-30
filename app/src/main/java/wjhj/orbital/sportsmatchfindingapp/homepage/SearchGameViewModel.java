package wjhj.orbital.sportsmatchfindingapp.homepage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class SearchGameViewModel extends ViewModel {
    private MutableLiveData<LocalTime> startTime = new MutableLiveData<>();
    private MutableLiveData<LocalTime> endTime = new MutableLiveData<>();
    private MutableLiveData<LocalDate> startDate = new MutableLiveData<>();
    private MutableLiveData<LocalDate> endDate = new MutableLiveData<>();

    public MutableLiveData<LocalTime> getStartTime() {
        return this.startTime;
    }

    public MutableLiveData<LocalTime> getEndTime() {
        return this.endTime;
    }

    public MutableLiveData<LocalDate> getStartDate() {
        return this.startDate;
    }

    public MutableLiveData<LocalDate> getEndDate() {
        return this.endDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime.setValue(startTime);
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime.setValue(endTime);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.setValue(startDate);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.setValue(endDate);
    }
}
