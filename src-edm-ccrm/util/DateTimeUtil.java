package edu.ccrm.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import edu.ccrm.domain.Semester;

/**
 * Utility class for date and time operations.
 * Demonstrates Java 8 Date/Time API usage.
 */
public class DateTimeUtil {
    public static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATETIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter BACKUP_TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private DateTimeUtil() {
        // Utility class, prevent instantiation
    }

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : "";
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : "";
    }

    public static String formatBackupTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(BACKUP_TIMESTAMP_FORMATTER) : "";
    }

    public static LocalDate parseDate(String dateStr) {
        return dateStr != null && !dateStr.trim().isEmpty() ? 
            LocalDate.parse(dateStr, DATE_FORMATTER) : null;
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return dateTimeStr != null && !dateTimeStr.trim().isEmpty() ? 
            LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER) : null;
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    public static boolean isWithinSemester(LocalDate date, Semester semester) {
        return !date.isBefore(semester.getStartDate()) && 
            !date.isAfter(semester.getEndDate());
    }

    public static String getRelativeTimeDescription(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        long days = ChronoUnit.DAYS.between(dateTime, now);

        if (minutes < 1) return "just now";
        if (minutes < 60) return minutes + " minutes ago";
        if (hours < 24) return hours + " hours ago";
        if (days < 30) return days + " days ago";
        return formatDate(dateTime.toLocalDate());
    }

    public static String generateBackupFolderName() {
        return "backup_" + formatBackupTimestamp(LocalDateTime.now());
    }

    public static LocalDateTime getStartOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

    public static LocalDateTime getEndOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    public static boolean isExpired(LocalDateTime dateTime, Duration timeout) {
        return dateTime.plus(timeout).isBefore(LocalDateTime.now());
    }
}