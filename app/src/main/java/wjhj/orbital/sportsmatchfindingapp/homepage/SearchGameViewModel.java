package wjhj.orbital.sportsmatchfindingapp.homepage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class SearchGameViewModel extends ViewModel {
    MutableLiveData<LocalTime> startTime = new MutableLiveData<>();
    MutableLiveData<LocalTime> endTime = new MutableLiveData<>();
    MutableLiveData<LocalDate> startDate = new MutableLiveData<>();
    MutableLiveData<LocalDate> endDate = new MutableLiveData<>();

    public void setStartTime(LocalTime startTime) {
        this.startTime.setValue(startTime);
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime.setValue(endTime);
    }

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

    public void setStartDate(LocalDate startDate) {
        this.startDate.setValue(startDate);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.setValue(endDate);
    }
}
