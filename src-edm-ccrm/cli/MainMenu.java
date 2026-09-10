package edu.ccrm.cli;

/**
 * Main menu implementation.
 * Demonstrates switch expressions and menu structure.
 */
public class MainMenu implements Menu {
    private final MenuHandler handler;

    public MainMenu(MenuHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getTitle() {
        return "Campus Course & Records Manager (CCRM)";
    }

    @Override
    public void display() {
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollments");
        System.out.println("4. Import/Export Data");
        System.out.println("5. Backup Operations");
        System.out.println("6. Generate Reports");
        System.out.println("7. About CCRM");
        System.out.println("8. Exit");
        System.out.print("\nEnter your choice: ");
    }

    @Override
    public void handleInput() {
        String choice = handler.getScanner().nextLine();

        switch (choice) {
            case "1" -> handler.navigateTo(new StudentMenu(handler));
            case "2" -> handler.navigateTo(new CourseMenu(handler));
            case "3" -> handler.navigateTo(new EnrollmentMenu(handler));
            case "4" -> handler.navigateTo(new DataMenu(handler));
            case "5" -> handler.navigateTo(new BackupMenu(handler));
            case "6" -> handler.navigateTo(new ReportMenu(handler));
            case "7" -> displayAbout();
            case "8" -> handler.exit();
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void displayAbout() {
        System.out.println("\n=== About CCRM ===");
        System.out.println("Campus Course & Records Manager");
        System.out.println("Version: 1.0");
        System.out.println("\nJava Platform Comparison:");
        System.out.println("- Java ME: Embedded and mobile devices");
        System.out.println("- Java SE: Standard desktop applications (This application)");
        System.out.println("- Java EE: Enterprise web applications");
        System.out.println("\nPress Enter to continue...");
        handler.getScanner().nextLine();
    }
}