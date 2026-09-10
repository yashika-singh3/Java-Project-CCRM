package edu.ccrm.io;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for data import/export operations.
 * Demonstrates NIO.2 Path usage.
 */
public interface DataPersistenceService {
    void exportData(Path directory) throws IOException;
    void importData(Path directory) throws IOException;
    void backup(Path backupDirectory) throws IOException;
    long getBackupSize(Path backupDirectory) throws IOException;
}