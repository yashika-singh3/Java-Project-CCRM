package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration class for the CCRM application.
 * Demonstrates singleton pattern and properties management.
 */
public class AppConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static volatile AppConfig instance;
    private final Properties properties;

    // Application settings with defaults
    private String dataDirectory = "data";
    private String backupDirectory = "backups";
    private int maxCreditsPerSemester = 18;
    private int maxBackupsToKeep = 5;
    private boolean debugMode = false;

    private AppConfig() {
        properties = new Properties();
        loadProperties();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                loadConfiguration();
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load configuration file. Using defaults.");
        }
    }

    private void loadConfiguration() {
        dataDirectory = properties.getProperty("data.directory", dataDirectory);
        backupDirectory = properties.getProperty("backup.directory", backupDirectory);
        maxCreditsPerSemester = Integer.parseInt(
            properties.getProperty("max.credits.per.semester", 
                String.valueOf(maxCreditsPerSemester)));
        maxBackupsToKeep = Integer.parseInt(
            properties.getProperty("max.backups.to.keep", 
                String.valueOf(maxBackupsToKeep)));
        debugMode = Boolean.parseBoolean(
            properties.getProperty("debug.mode", 
                String.valueOf(debugMode)));
    }

    public Path getDataDirectory() {
        return Paths.get(dataDirectory).toAbsolutePath();
    }

    public Path getBackupDirectory() {
        return Paths.get(backupDirectory).toAbsolutePath();
    }

    public int getMaxCreditsPerSemester() {
        return maxCreditsPerSemester;
    }

    public int getMaxBackupsToKeep() {
        return maxBackupsToKeep;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public void setBackupDirectory(String backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    public void setMaxCreditsPerSemester(int maxCreditsPerSemester) {
        this.maxCreditsPerSemester = maxCreditsPerSemester;
    }

    public void setMaxBackupsToKeep(int maxBackupsToKeep) {
        this.maxBackupsToKeep = maxBackupsToKeep;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    // Debug logging utility method
    public void debug(String message) {
        if (debugMode) {
            System.out.printf("[DEBUG] %s%n", message);
        }
    }
}