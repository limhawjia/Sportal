package wjhj.orbital.sportsmatchfindingapp.game;

import org.threeten.bp.LocalTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.time.LocalDateTime;

public enum TimeOfDay {


    MORNING("morning", LocalTime.of(6, 0), LocalTime.of(11, 59) ),
    AFTERNOON("afternoon", LocalTime.of(12, 0), LocalTime.of(18, 59)),
    NIGHT("night", LocalTime.of(19, 0), LocalTime.of(5, 59));

    private String name;
    private LocalTime startTime;
    private LocalTime endTime;

    TimeOfDay(String name, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimeOfDay getTimeOfDay(LocalTime time) {
        LocalTime midnight = LocalTime.of(0, 0);
        if (time.isAfter(midnight) && time.isBefore(MORNING.startTime)) {
            return NIGHT;
        } else if (time.isAfter(NIGHT.endTime) && time.isBefore(AFTERNOON.startTime)) {
            return MORNING;
        } else if (time.isAfter(MORNING.endTime) && time.isBefore(NIGHT.startTime)) {
            return AFTERNOON;
        } else {
            return NIGHT;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
