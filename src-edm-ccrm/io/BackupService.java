package edu.ccrm.io;

import edu.ccrm.util.RecursiveFileUtil;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for managing data backups using NIO.2.
 * Demonstrates file operations, walking directory trees, and file attributes.
 */
public class BackupService {
    private final Path backupRoot;
    private final ImportExportService importExportService;
    private static final String BACKUP_PREFIX = "backup_";
    private static final DateTimeFormatter BACKUP_TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public BackupService(Path backupRoot, ImportExportService importExportService) {
        this.backupRoot = backupRoot;
        this.importExportService = importExportService;
    }

    /**
     * Creates a new backup with current timestamp
     */
    public Path createBackup() throws IOException {
        String timestamp = LocalDateTime.now().format(BACKUP_TIMESTAMP_FORMAT);
        Path backupDir = backupRoot.resolve(BACKUP_PREFIX + timestamp);
        
        // Create backup directory
        Files.createDirectories(backupDir);
        
        // Perform backup using ImportExportService
        importExportService.backup(backupDir);
        
        return backupDir;
    }

    /**
     * Lists all backup directories ordered by timestamp
     */
    public List<Path> listBackups() throws IOException {
        try (Stream<Path> paths = Files.list(backupRoot)) {
            return paths
                .filter(path -> Files.isDirectory(path) && 
                    path.getFileName().toString().startsWith(BACKUP_PREFIX))
                .sorted()
                .collect(Collectors.toList());
        }
    }

    /**
     * Restores data from a specific backup
     */
    public void restoreFromBackup(Path backupDir) throws IOException {
        if (!Files.exists(backupDir) || !Files.isDirectory(backupDir)) {
            throw new IllegalArgumentException("Invalid backup directory");
        }
        importExportService.importData(backupDir);
    }

    /**
     * Removes old backups keeping only the specified number of recent ones
     */
    public void cleanupOldBackups(int keepCount) throws IOException {
        List<Path> backups = listBackups();
        if (backups.size() > keepCount) {
            for (int i = 0; i < backups.size() - keepCount; i++) {
                deleteBackup(backups.get(i));
            }
        }
    }

    /**
     * Recursively calculates the size of a backup directory
     */
    public long calculateBackupSize(Path backupDir) throws IOException {
        return importExportService.getBackupSize(backupDir);
    }

    /**
     * Recursively deletes a backup directory
     */
    private void deleteBackup(Path backupDir) throws IOException {
        Files.walkFileTree(backupDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
                    throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
                    throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Verifies the integrity of a backup directory
     */
    public boolean verifyBackup(Path backupDir) throws IOException {
        if (!Files.exists(backupDir) || !Files.isDirectory(backupDir)) {
            return false;
        }

        // Check for required files
        return Stream.of("students.csv", "courses.csv", "enrollments.csv")
            .allMatch(filename -> Files.exists(backupDir.resolve(filename)));
    }
}