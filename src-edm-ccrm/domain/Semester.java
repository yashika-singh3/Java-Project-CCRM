package edu.ccrm.domain;

import java.time.LocalDate;
import java.time.Month;

/**
 * Enum representing academic semesters.
 * Demonstrates enum with constructor, fields, and methods.
 */
public enum Semester {
    FALL_2025(2025, Month.SEPTEMBER, Month.DECEMBER),
    SPRING_2026(2026, Month.JANUARY, Month.MAY),
    SUMMER_2026(2026, Month.JUNE, Month.AUGUST);

    private final int year;
    private final Month startMonth;
    private final Month endMonth;

    Semester(int year, Month startMonth, Month endMonth) {
        this.year = year;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
    }

    public LocalDate getStartDate() {
        return LocalDate.of(year, startMonth, 1);
    }

    public LocalDate getEndDate() {
        return LocalDate.of(year, endMonth, endMonth.length(year % 4 == 0));
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(getStartDate()) && !now.isAfter(getEndDate());
    }

    public static Semester getCurrentSemester() {
        LocalDate now = LocalDate.now();
        for (Semester semester : values()) {
            if (semester.isActive()) {
                return semester;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s - %s %d)",
            name(), startMonth, endMonth, year);
    }
}