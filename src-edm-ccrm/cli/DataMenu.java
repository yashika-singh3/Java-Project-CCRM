package edu.ccrm.cli;

import edu.ccrm.io.ImportExportService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Menu for data import/export operations.
 */
public class DataMenu implements Menu {
    private final MenuHandler handler;
    private final ImportExportService importExportService;
    private final Scanner scanner;

    public DataMenu(MenuHandler handler) {
        this.handler = handler;
        this.importExportService = handler.getImportExportService();
        this.scanner = handler.getScanner();
    }

    @Override
    public String getTitle() {
        return "Data Import/Export";
    }

    @Override
    public void display() {
        System.out.println("1. Import Data");
        System.out.println("2. Export Data");
        System.out.println("3. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
    }

    @Override
    public void handleInput() {
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> importData();
            case "2" -> exportData();
            case "3" -> handler.goBack();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void importData() {
        System.out.println("\n=== Import Data ===");
        System.out.print("Enter data directory path: ");
        String path = scanner.nextLine();
        
        try {
            importExportService.importData(Path.of(path));
            System.out.println("Data imported successfully!");
        } catch (IOException e) {
            System.out.println("Error importing data: " + e.getMessage());
        }
    }

    private void exportData() {
        System.out.println("\n=== Export Data ===");
        System.out.print("Enter export directory path: ");
        String path = scanner.nextLine();
        
        try {
            importExportService.exportData(Path.of(path));
            System.out.println("Data exported successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting data: " + e.getMessage());
        }
    }
}