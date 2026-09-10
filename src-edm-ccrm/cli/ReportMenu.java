package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.service.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Menu for generating various reports.
 * Demonstrates Stream API for data analysis.
 */
public class ReportMenu implements Menu {
    private final MenuHandler handler;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final Scanner scanner;

    public ReportMenu(MenuHandler handler) {
        this.handler = handler;
        this.studentService = handler.getStudentService();
        this.courseService = handler.getCourseService();
        this.enrollmentService = handler.getEnrollmentService();
        this.scanner = handler.getScanner();
    }

    @Override
    public String getTitle() {
        return "Reports";
    }

    @Override
    public void display() {
        System.out.println("1. GPA Distribution");
        System.out.println("2. Course Enrollment Statistics");
        System.out.println("3. Department Performance Report");
        System.out.println("4. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
    }

    @Override
    public void handleInput() {
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> showGpaDistribution();
            case "2" -> showCourseEnrollmentStats();
            case "3" -> showDepartmentPerformance();
            case "4" -> handler.goBack();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void showGpaDistribution() {
        System.out.println("\n=== GPA Distribution ===");
        
        // Group students by GPA ranges using Stream API
        Map<String, Long> distribution = studentService.findAll().stream()
            .filter(Student::isActive)
            .collect(Collectors.groupingBy(
                student -> {
                    double gpa = student.getGpa();
                    if (gpa >= 3.5) return "3.5 - 4.0";
                    if (gpa >= 3.0) return "3.0 - 3.49";
                    if (gpa >= 2.5) return "2.5 - 2.99";
                    if (gpa >= 2.0) return "2.0 - 2.49";
                    return "Below 2.0";
                },
                Collectors.counting()
            ));

        // Print distribution
        distribution.forEach((range, count) ->
            System.out.printf("%s: %d students%n", range, count));
    }

    private void showCourseEnrollmentStats() {
        System.out.println("\n=== Course Enrollment Statistics ===");
        
        courseService.findAll().stream()
            .filter(Course::isActive)
            .forEach(course -> {
                List<Enrollment> enrollments = enrollmentService.findByCourse(course);
                long activeEnrollments = enrollments.stream()
                    .filter(e -> e.getStatus() == Enrollment.EnrollmentStatus.ENROLLED)
                    .count();
                
                System.out.printf("%s (%s):%n", course.getCode(), course.getTitle());
                System.out.printf("  Total Enrollments: %d%n", enrollments.size());
                System.out.printf("  Active Enrollments: %d%n", activeEnrollments);
                
                // Calculate grade distribution
                Map<Grade, Long> gradeDistribution = enrollments.stream()
                    .filter(e -> e.getGrade() != null)
                    .collect(Collectors.groupingBy(
                        Enrollment::getGrade,
                        Collectors.counting()
                    ));
                
                if (!gradeDistribution.isEmpty()) {
                    System.out.println("  Grade Distribution:");
                    gradeDistribution.forEach((grade, count) ->
                        System.out.printf("    %s: %d%n", grade.name(), count));
                }
                System.out.println();
            });
    }

    private void showDepartmentPerformance() {
        System.out.println("\n=== Department Performance Report ===");
        
        // Group courses by department
        Map<String, List<Course>> coursesByDept = courseService.findAll().stream()
            .filter(Course::isActive)
            .collect(Collectors.groupingBy(Course::getDepartment));
        
        coursesByDept.forEach((dept, courses) -> {
            System.out.printf("%n=== %s Department ===%n", dept);
            
            // Calculate department statistics
            int totalStudents = 0;
            double totalGpa = 0.0;
            int gradedEnrollments = 0;
            
            for (Course course : courses) {
                List<Enrollment> enrollments = enrollmentService.findByCourse(course);
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getGrade() != null) {
                        totalGpa += enrollment.getGrade().getPoints();
                        gradedEnrollments++;
                    }
                    if (enrollment.getStatus() == Enrollment.EnrollmentStatus.ENROLLED) {
                        totalStudents++;
                    }
                }
            }
            
            System.out.printf("Courses Offered: %d%n", courses.size());
            System.out.printf("Total Active Students: %d%n", totalStudents);
            if (gradedEnrollments > 0) {
                System.out.printf("Average GPA: %.2f%n", totalGpa / gradedEnrollments);
            }
        });
    }
}