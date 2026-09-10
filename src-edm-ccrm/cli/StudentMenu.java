package edu.ccrm.cli;

import edu.ccrm.domain.Student;
import edu.ccrm.service.StudentService;
import java.util.List;
import java.util.Scanner;

/**
 * Menu for student management operations.
 */
public class StudentMenu implements Menu {
    private final MenuHandler handler;
    private final StudentService studentService;
    private final Scanner scanner;

    public StudentMenu(MenuHandler handler) {
        this.handler = handler;
        this.studentService = handler.getStudentService();
        this.scanner = handler.getScanner();
    }

    @Override
    public String getTitle() {
        return "Student Management";
    }

    @Override
    public void display() {
        System.out.println("1. Add New Student");
        System.out.println("2. List All Students");
        System.out.println("3. Find Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Deactivate Student");
        System.out.println("6. View Top Performers");
        System.out.println("7. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
    }

    @Override
    public void handleInput() {
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addStudent();
            case "2" -> listStudents();
            case "3" -> findStudent();
            case "4" -> updateStudent();
            case "5" -> deactivateStudent();
            case "6" -> viewTopPerformers();
            case "7" -> handler.goBack();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void addStudent() {
        System.out.println("\n=== Add New Student ===");
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Registration Number: ");
        String regNo = scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        Student student = new Student(id, fullName, email, regNo);
        studentService.save(student);
        System.out.println("Student added successfully!");
    }

    private void listStudents() {
        System.out.println("\n=== All Students ===");
        List<Student> students = studentService.findAll();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(System.out::println);
        }
    }

    private void findStudent() {
        System.out.print("\nEnter Student ID: ");
        String id = scanner.nextLine();
        Student student = studentService.findById(id);
        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("Student not found.");
        }
    }

    private void updateStudent() {
        System.out.print("\nEnter Student ID to update: ");
        String id = scanner.nextLine();
        Student student = studentService.findById(id);
        if (student != null) {
            System.out.print("Enter new Full Name (or press Enter to skip): ");
            String fullName = scanner.nextLine();
            if (!fullName.isEmpty()) {
                student.setFullName(fullName);
            }

            System.out.print("Enter new Email (or press Enter to skip): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                student.setEmail(email);
            }

            studentService.save(student);
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Student not found.");
        }
    }

    private void deactivateStudent() {
        System.out.print("\nEnter Student ID to deactivate: ");
        String id = scanner.nextLine();
        studentService.deactivateStudent(id);
        System.out.println("Student deactivated successfully!");
    }

    private void viewTopPerformers() {
        System.out.print("\nEnter number of top performers to view: ");
        try {
            int limit = Integer.parseInt(scanner.nextLine());
            List<Student> topPerformers = studentService.findTopPerformers(limit);
            System.out.println("\n=== Top " + limit + " Performers ===");
            topPerformers.forEach(student -> 
                System.out.printf("%s - GPA: %.2f%n", student.getFullName(), student.getGpa()));
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}