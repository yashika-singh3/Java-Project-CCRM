package edu.ccrm;

import edu.ccrm.cli.MenuHandler;
import edu.ccrm.config.AppConfig;
import edu.ccrm.io.*;
import edu.ccrm.service.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main application class for the Campus Course & Records Manager (CCRM).
 */
public class CCRMApp {
    public static void main(String[] args) {
        try {
            // Initialize configuration
            AppConfig config = AppConfig.getInstance();
            
            // Create data and backup directories if they don't exist
            Files.createDirectories(config.getDataDirectory());
            Files.createDirectories(config.getBackupDirectory());

            // Initialize services
            StudentService studentService = new StudentServiceImpl();
            CourseService courseService = new CourseServiceImpl();
            EnrollmentService enrollmentService = new EnrollmentServiceImpl();
            
            ImportExportService importExportService = new ImportExportService(
                studentService, courseService, enrollmentService);
            
            BackupService backupService = new BackupService(
                config.getBackupDirectory(), importExportService);

            // Initialize and start the menu handler
            MenuHandler menuHandler = new MenuHandler(
                studentService,
                courseService,
                enrollmentService,
                importExportService,
                backupService,
                config
            );

            // Start the application
            System.out.println("Welcome to the Campus Course & Records Manager (CCRM)");
            menuHandler.start();

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            if (AppConfig.getInstance().isDebugMode()) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }
}