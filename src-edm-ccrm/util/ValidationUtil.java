package edu.ccrm.util;

import edu.ccrm.domain.*;
import java.util.regex.Pattern;

/**
 * Utility class for input validation.
 * Demonstrates input validation and regular expressions.
 */
public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern COURSE_CODE_PATTERN = 
        Pattern.compile("^[A-Z]{2,4}\\d{3}$");
    private static final Pattern STUDENT_ID_PATTERN = 
        Pattern.compile("^\\d{8}$");

    private ValidationUtil() {
        // Utility class, prevent instantiation
    }

    // Email validation
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    // Course code validation (e.g., CS101, MATH201)
    public static boolean isValidCourseCode(String code) {
        return code != null && COURSE_CODE_PATTERN.matcher(code).matches();
    }

    // Student ID validation (8 digits)
    public static boolean isValidStudentId(String id) {
        return id != null && STUDENT_ID_PATTERN.matcher(id).matches();
    }

    // Credit hours validation
    public static boolean isValidCredits(int credits) {
        return credits >= 1 && credits <= 6;
    }

    // Grade value validation
    public static boolean isValidGrade(double grade) {
        return grade >= 0.0 && grade <= 4.0;
    }

    // Student name validation
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }

    // Department name validation
    public static boolean isValidDepartment(String department) {
        return department != null && !department.trim().isEmpty() && 
            department.length() <= 50;
    }

    // Enrollment validation
    public static boolean canEnroll(Student student, Course course) {
        return student != null && course != null && 
            student.isActive() && course.isActive();
    }

    // Null checks with meaningful messages
    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    // Range validation
    public static void validateRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                String.format("%s must be between %d and %d", fieldName, min, max));
        }
    }

    // String length validation
    public static void validateLength(String value, int maxLength, String fieldName) {
        if (value == null || value.length() > maxLength) {
            throw new IllegalArgumentException(
                String.format("%s must not be null and must be at most %d characters long", 
                    fieldName, maxLength));
        }
    }

    // Active status validation
    public static void validateActive(boolean isActive, String type) {
        if (!isActive) {
            throw new IllegalStateException(
                String.format("%s is not active", type));
        }
    }
}