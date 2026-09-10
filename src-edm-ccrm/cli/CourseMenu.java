package edu.ccrm.cli;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import edu.ccrm.service.CourseService;
import java.util.List;
import java.util.Scanner;

/**
 * Menu for course management operations.
 */
public class CourseMenu implements Menu {
    private final MenuHandler handler;
    private final CourseService courseService;
    private final Scanner scanner;

    public CourseMenu(MenuHandler handler) {
        this.handler = handler;
        this.courseService = handler.getCourseService();
        this.scanner = handler.getScanner();
    }

    @Override
    public String getTitle() {
        return "Course Management";
    }

    @Override
    public void display() {
        System.out.println("1. Add New Course");
        System.out.println("2. List All Courses");
        System.out.println("3. Find Course by Code");
        System.out.println("4. Update Course");
        System.out.println("5. Deactivate Course");
        System.out.println("6. Filter Courses");
        System.out.println("7. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
    }

    @Override
    public void handleInput() {
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addCourse();
            case "2" -> listCourses();
            case "3" -> findCourse();
            case "4" -> updateCourse();
            case "5" -> deactivateCourse();
            case "6" -> filterCourses();
            case "7" -> handler.goBack();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void addCourse() {
        System.out.println("\n=== Add New Course ===");
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Credits: ");
        int credits = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        System.out.println("Available Semesters:");
        for (Semester semester : Semester.values()) {
            System.out.println("- " + semester.name());
        }
        System.out.print("Enter Semester: ");
        Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());

        Course course = new Course.Builder(code)
            .title(title)
            .credits(credits)
            .department(department)
            .semester(semester)
            .build();

        courseService.save(course);
        System.out.println("Course added successfully!");
    }

    private void listCourses() {
        System.out.println("\n=== All Courses ===");
        List<Course> courses = courseService.findAll();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            courses.forEach(System.out::println);
        }
    }

    private void findCourse() {
        System.out.print("\nEnter Course Code: ");
        String code = scanner.nextLine();
        Course course = courseService.findById(code);
        if (course != null) {
            System.out.println(course);
        } else {
            System.out.println("Course not found.");
        }
    }

    private void updateCourse() {
        System.out.print("\nEnter Course Code to update: ");
        String code = scanner.nextLine();
        Course course = courseService.findById(code);
        if (course != null) {
            System.out.print("Enter new Title (or press Enter to skip): ");
            String title = scanner.nextLine();
            if (!title.isEmpty()) {
                course.setTitle(title);
            }

            System.out.print("Enter new Credits (or press Enter to skip): ");
            String credits = scanner.nextLine();
            if (!credits.isEmpty()) {
                course.setCredits(Integer.parseInt(credits));
            }

            System.out.print("Enter new Department (or press Enter to skip): ");
            String department = scanner.nextLine();
            if (!department.isEmpty()) {
                course.setDepartment(department);
            }

            courseService.save(course);
            System.out.println("Course updated successfully!");
        } else {
            System.out.println("Course not found.");
        }
    }

    private void deactivateCourse() {
        System.out.print("\nEnter Course Code to deactivate: ");
        String code = scanner.nextLine();
        courseService.deactivateCourse(code);
        System.out.println("Course deactivated successfully!");
    }

    private void filterCourses() {
        System.out.println("\n=== Filter Courses ===");
        System.out.println("1. By Department");
        System.out.println("2. By Semester");
        System.out.println("3. By Instructor");
        System.out.print("Enter filter choice: ");

        String choice = scanner.nextLine();
        List<Course> filteredCourses = switch (choice) {
            case "1" -> {
                System.out.print("Enter Department: ");
                yield courseService.findByDepartment(scanner.nextLine());
            }
            case "2" -> {
                System.out.print("Enter Semester (e.g., FALL_2025): ");
                yield courseService.findBySemester(Semester.valueOf(scanner.nextLine().toUpperCase()));
            }
            case "3" -> {
                System.out.print("Enter Instructor ID: ");
                String instructorId = scanner.nextLine();
                Instructor instructor = null; // TODO: Add InstructorService to get instructor
                yield instructor != null ? courseService.findByInstructor(instructor) : List.of();
            }
            default -> {
                System.out.println("Invalid choice.");
                yield List.of();
            }
        };

        if (filteredCourses.isEmpty()) {
            System.out.println("No courses found matching the criteria.");
        } else {
            filteredCourses.forEach(System.out::println);
        }
    }
}