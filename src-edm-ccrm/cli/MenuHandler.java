package edu.ccrm.cli;

import edu.ccrm.service.*;
import edu.ccrm.io.*;
import edu.ccrm.config.AppConfig;
import java.util.Scanner;
import java.util.Stack;

/**
 * Main handler for CLI menu system.
 * Demonstrates menu navigation and user interaction.
 */
public class MenuHandler {
    private final Stack<Menu> menuStack;
    private final Scanner scanner;
    private boolean running;

    // Services
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final ImportExportService importExportService;
    private final BackupService backupService;
    private final AppConfig config;

    public MenuHandler(
            StudentService studentService,
            CourseService courseService,
            EnrollmentService enrollmentService,
            ImportExportService importExportService,
            BackupService backupService,
            AppConfig config) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.importExportService = importExportService;
        this.backupService = backupService;
        this.config = config;
        this.menuStack = new Stack<>();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public void start() {
        // Push main menu as the starting point
        menuStack.push(new MainMenu(this));

        while (running && !menuStack.isEmpty()) {
            Menu currentMenu = menuStack.peek();
            System.out.println("\n=== " + currentMenu.getTitle() + " ===");
            currentMenu.display();
            currentMenu.handleInput();
        }
    }

    public void navigateTo(Menu menu) {
        menuStack.push(menu);
    }

    public void goBack() {
        if (!menuStack.isEmpty()) {
            menuStack.pop();
        }
    }

    public void exit() {
        running = false;
    }

    public Scanner getScanner() {
        return scanner;
    }

    // Getters for services
    public StudentService getStudentService() {
        return studentService;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public EnrollmentService getEnrollmentService() {
        return enrollmentService;
    }

    public ImportExportService getImportExportService() {
        return importExportService;
    }

    public BackupService getBackupService() {
        return backupService;
    }

    public AppConfig getConfig() {
        return config;
    }
}