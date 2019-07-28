package wjhj.orbital.sportsmatchfindingapp.utils;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class DateUtils {
    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private DateUtils() {}

    public static String formatTime(long timeInMillis) {
        LocalDateTime localDateTime = toLocalDateTime(timeInMillis);
        return localDateTime.toLocalTime().format(TIME_FORMATTER);
    }

    public static String formatDate(long timeInMillis) {
        LocalDateTime localDateTime = toLocalDateTime(timeInMillis);
        return localDateTime.toLocalDate().format(DATE_FORMATTER);
    }

    public static String formatDateTimeForChat(long timeInMillis) {
        if (isToday(timeInMillis)) {
            return formatTime(timeInMillis);
        } else {
            return formatDate(timeInMillis);
        }

    }

    public static LocalDateTime toLocalDateTime(long timeInMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), ZoneId.systemDefault());
    }

    public static boolean isToday(long timeInMillis) {
        LocalDateTime localDateTime = toLocalDateTime(timeInMillis);
        return localDateTime.toLocalDate().isEqual(LocalDate.now());
    }
}
