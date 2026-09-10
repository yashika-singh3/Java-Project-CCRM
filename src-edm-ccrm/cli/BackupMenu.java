package edu.ccrm.cli;

import edu.ccrm.io.BackupService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Menu for backup operations.
 */
public class BackupMenu implements Menu {
    private final MenuHandler handler;
    private final BackupService backupService;
    private final Scanner scanner;

    public BackupMenu(MenuHandler handler) {
        this.handler = handler;
        this.backupService = handler.getBackupService();
        this.scanner = handler.getScanner();
    }

    @Override
    public String getTitle() {
        return "Backup Operations";
    }

    @Override
    public void display() {
        System.out.println("1. Create New Backup");
        System.out.println("2. List Backups");
        System.out.println("3. Restore from Backup");
        System.out.println("4. Calculate Backup Size");
        System.out.println("5. Clean Old Backups");
        System.out.println("6. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
    }

    @Override
    public void handleInput() {
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> createBackup();
            case "2" -> listBackups();
            case "3" -> restoreBackup();
            case "4" -> calculateBackupSize();
            case "5" -> cleanOldBackups();
            case "6" -> handler.goBack();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void createBackup() {
        System.out.println("\n=== Create New Backup ===");
        try {
            Path backupPath = backupService.createBackup();
            System.out.println("Backup created successfully at: " + backupPath);
        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }

    private void listBackups() {
        System.out.println("\n=== Available Backups ===");
        try {
            List<Path> backups = backupService.listBackups();
            if (backups.isEmpty()) {
                System.out.println("No backups found.");
            } else {
                backups.forEach(System.out::println);
            }
        } catch (IOException e) {
            System.out.println("Error listing backups: " + e.getMessage());
        }
    }

    private void restoreBackup() {
        System.out.println("\n=== Restore from Backup ===");
        try {
            List<Path> backups = backupService.listBackups();
            if (backups.isEmpty()) {
                System.out.println("No backups available.");
                return;
            }

            System.out.println("Available backups:");
            for (int i = 0; i < backups.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, backups.get(i).getFileName());
            }

            System.out.print("Enter backup number to restore: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < backups.size()) {
                    backupService.restoreFromBackup(backups.get(choice));
                    System.out.println("Backup restored successfully!");
                } else {
                    System.out.println("Invalid backup number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (IOException e) {
            System.out.println("Error restoring backup: " + e.getMessage());
        }
    }

    private void calculateBackupSize() {
        System.out.println("\n=== Calculate Backup Size ===");
        try {
            List<Path> backups = backupService.listBackups();
            if (backups.isEmpty()) {
                System.out.println("No backups found.");
                return;
            }

            for (Path backup : backups) {
                long size = backupService.calculateBackupSize(backup);
                System.out.printf("%s: %d bytes%n", backup.getFileName(), size);
            }
        } catch (IOException e) {
            System.out.println("Error calculating backup size: " + e.getMessage());
        }
    }

    private void cleanOldBackups() {
        System.out.println("\n=== Clean Old Backups ===");
        System.out.print("Enter number of recent backups to keep: ");
        try {
            int keepCount = Integer.parseInt(scanner.nextLine());
            backupService.cleanupOldBackups(keepCount);
            System.out.println("Old backups cleaned successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (IOException e) {
            System.out.println("Error cleaning old backups: " + e.getMessage());
        }
    }
}