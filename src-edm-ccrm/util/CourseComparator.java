package edu.ccrm.util;

import edu.ccrm.domain.Course;
import java.util.Comparator;
import java.util.function.Function;

/**
 * Utility class providing various Course comparators.
 * Demonstrates functional interfaces and lambda expressions.
 */
public class CourseComparator {
    private CourseComparator() {
        // Utility class, prevent instantiation
    }

    // Basic comparators
    public static final Comparator<Course> BY_CODE = 
        Comparator.comparing(Course::getCode);

    public static final Comparator<Course> BY_TITLE = 
        Comparator.comparing(Course::getTitle);

    public static final Comparator<Course> BY_CREDITS = 
        Comparator.comparing(Course::getCredits);

    public static final Comparator<Course> BY_DEPARTMENT = 
        Comparator.comparing(Course::getDepartment);

    public static final Comparator<Course> BY_SEMESTER = 
        Comparator.comparing(Course::getSemester);

    // Complex comparators
    public static final Comparator<Course> BY_DEPARTMENT_THEN_CODE = 
        BY_DEPARTMENT.thenComparing(BY_CODE);

    public static final Comparator<Course> BY_SEMESTER_THEN_DEPARTMENT = 
        BY_SEMESTER.thenComparing(BY_DEPARTMENT);

    // Dynamic comparator creation
    public static <T extends Comparable<T>> Comparator<Course> byFieldThenCode(Function<Course, T> fieldExtractor) {
        return Comparator.comparing(fieldExtractor).thenComparing(BY_CODE);
    }

    // Specialized comparators
    public static Comparator<Course> byInstructorName() {
        return Comparator.comparing(
            course -> course.getInstructor() != null ? 
                course.getInstructor().getFullName() : ""
        );
    }

    public static Comparator<Course> byActiveStatusThenCode() {
        return Comparator.comparing(Course::isActive).reversed()
            .thenComparing(BY_CODE);
    }

    // Natural order is by code
    public static Comparator<Course> naturalOrder() {
        return BY_CODE;
    }

    public static Comparator<Course> reverseOrder() {
        return BY_CODE.reversed();
    }
}