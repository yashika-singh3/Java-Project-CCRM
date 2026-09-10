package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import edu.ccrm.service.EnrollmentService;
import java.util.List;
import java.util.Scanner;

/**
 * Menu for enrollment management operations.
 */
public class EnrollmentMenu implements Menu {
    private final MenuHandler handler;
    private final EnrollmentService enrollmentService;
    private final Scanner scanner;

    public EnrollmentMenu(MenuHandler handler) {
        this.handler = handler;
        this.enrollmentService = handler.getEnrollmentService();
        this.scanner = handler.getScanner();
    }

    @Override
    public String getTitle() {
        return "Enrollment Management";
    }

    @Override
    public void display() {
        System.out.println("1. Enroll Student in Course");
        System.out.println("2. Withdraw from Course");
        System.out.println("3. Assign Grade");
        System.out.println("4. View Student Enrollments");
        System.out.println("5. View Course Enrollments");
        System.out.println("6. Calculate Student GPA");
        System.out.println("7. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
    }

    @Override
    public void handleInput() {
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> enrollStudent();
            case "2" -> withdrawStudent();
            case "3" -> assignGrade();
            case "4" -> viewStudentEnrollments();
            case "5" -> viewCourseEnrollments();
            case "6" -> calculateGPA();
            case "7" -> handler.goBack();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void enrollStudent() {
        System.out.println("\n=== Enroll Student in Course ===");
        
        // Get student
        System.out.print("Enter Student ID: ");
        Student student = handler.getStudentService().findById(scanner.nextLine());
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Get course
        System.out.print("Enter Course Code: ");
        Course course = handler.getCourseService().findById(scanner.nextLine());
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        try {
            enrollmentService.enroll(student, course);
            System.out.println("Student enrolled successfully!");
        } catch (DuplicateEnrollmentException e) {
            System.out.println("Error: Student is already enrolled in this course.");
        } catch (MaxCreditLimitExceededException e) {
            System.out.println("Error: Enrolling would exceed maximum credit limit.");
        }
    }

    private void withdrawStudent() {
        System.out.println("\n=== Withdraw from Course ===");
        
        // Get student
        System.out.print("Enter Student ID: ");
        Student student = handler.getStudentService().findById(scanner.nextLine());
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Get course
        System.out.print("Enter Course Code: ");
        Course course = handler.getCourseService().findById(scanner.nextLine());
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        enrollmentService.withdraw(student, course);
        System.out.println("Student withdrawn successfully!");
    }

    private void assignGrade() {
        System.out.println("\n=== Assign Grade ===");
        
        // Get student
        System.out.print("Enter Student ID: ");
        Student student = handler.getStudentService().findById(scanner.nextLine());
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Get course
        System.out.print("Enter Course Code: ");
        Course course = handler.getCourseService().findById(scanner.nextLine());
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        // Display available grades
        System.out.println("\nAvailable Grades:");
        for (Grade grade : Grade.values()) {
            System.out.printf("%s (%s, %.1f points)%n", 
                grade.name(), grade.getDescription(), grade.getPoints());
        }

        // Get grade
        System.out.print("Enter Grade: ");
        try {
            Grade grade = Grade.valueOf(scanner.nextLine().toUpperCase());
            enrollmentService.assignGrade(student, course, grade);
            System.out.println("Grade assigned successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid grade entered.");
        }
    }

    private void viewStudentEnrollments() {
        System.out.print("\nEnter Student ID: ");
        Student student = handler.getStudentService().findById(scanner.nextLine());
        if (student != null) {
            List<Enrollment> enrollments = enrollmentService.findByStudent(student);
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found for this student.");
            } else {
                System.out.printf("\nEnrollments for %s:%n", student.getFullName());
                enrollments.forEach(System.out::println);
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    private void viewCourseEnrollments() {
        System.out.print("\nEnter Course Code: ");
        Course course = handler.getCourseService().findById(scanner.nextLine());
        if (course != null) {
            List<Enrollment> enrollments = enrollmentService.findByCourse(course);
            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found for this course.");
            } else {
                System.out.printf("\nEnrollments for %s:%n", course.getTitle());
                enrollments.forEach(System.out::println);
            }
        } else {
            System.out.println("Course not found.");
        }
    }

    private void calculateGPA() {
        System.out.print("\nEnter Student ID: ");
        Student student = handler.getStudentService().findById(scanner.nextLine());
        if (student != null) {
            System.out.println("\nAvailable Semesters:");
            for (Semester semester : Semester.values()) {
                System.out.println("- " + semester.name());
            }
            System.out.print("Enter Semester: ");
            try {
                Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());
                double gpa = enrollmentService.calculateGpa(student, semester);
                System.out.printf("%s's GPA for %s: %.2f%n", 
                    student.getFullName(), semester, gpa);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid semester entered.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }
}