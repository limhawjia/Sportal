package wjhj.orbital.sportsmatchfindingapp.utils;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class DateUtils {
    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static DateTimeFormatter DDMM_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
    public static DateTimeFormatter DDMMYYYY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateUtils() {
    }

    public static String formatTime(long timeInMillis) {
        LocalDateTime localDateTime = toLocalDateTime(timeInMillis);
        return localDateTime.toLocalTime().format(TIME_FORMATTER);
    }

    public static String formatDateDDMM(long timeInMillis) {

        LocalDateTime localDateTime = toLocalDateTime(timeInMillis);
        return localDateTime.toLocalDate().format(DDMM_FORMATTER);
    }

    public static String formatDateDDMMYYYY(long timeInMillis) {
        LocalDateTime localDateTime = toLocalDateTime(timeInMillis);
        return localDateTime.toLocalDate().format(DDMMYYYY_FORMATTER);
    }

    public static String formatDateTimeForChat(long timeInMillis) {
        if (isToday(timeInMillis)) {
            return formatTime(timeInMillis);
        } else if (isThisYear(timeInMillis)) {
            return formatDateDDMM(timeInMillis);
        } else {
            return formatDateDDMMYYYY(timeInMillis);
        }

    }

    public static LocalDateTime toLocalDateTime(long timeInMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), ZoneId.systemDefault());
    }

    public static boolean isToday(long timeInMillis) {
        LocalDateTime localDateTime = toLocalDateTime(timeInMillis);
        return localDateTime.toLocalDate().isEqual(LocalDate.now());
    }

    public static boolean isThisYear(long timeInMillis) {
        LocalDateTime localDateTime = toLocalDateTime(timeInMillis);
        return localDateTime.getYear() == LocalDate.now().getYear();
    }
}
