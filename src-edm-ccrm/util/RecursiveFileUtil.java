package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Utility class for recursive file operations using NIO.2.
 * Demonstrates recursive operations and visitor pattern.
 */
public class RecursiveFileUtil {
    private RecursiveFileUtil() {
        // Utility class, prevent instantiation
    }

    /**
     * Recursively finds files matching the given predicate
     */
    public static List<Path> findFiles(Path startPath, Predicate<Path> predicate) 
            throws IOException {
        List<Path> result = new ArrayList<>();
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (predicate.test(file)) {
                    result.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return result;
    }

    /**
     * Recursively calculates total size of files in a directory
     */
    public static long calculateTotalSize(Path directory) throws IOException {
        return Files.walk(directory)
            .filter(Files::isRegularFile)
            .mapToLong(path -> {
                try {
                    return Files.size(path);
                } catch (IOException e) {
                    return 0L;
                }
            })
            .sum();
    }

    /**
     * Recursively copies a directory
     */
    public static void copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) 
                    throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
                    throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)),
                    StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Recursively lists files with their relative paths and sizes
     */
    public static List<String> listFilesWithDetails(Path directory) throws IOException {
        List<String> result = new ArrayList<>();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
                    throws IOException {
                result.add(String.format("%s (%d bytes)",
                    directory.relativize(file),
                    attrs.size()));
                return FileVisitResult.CONTINUE;
            }
        });
        return result;
    }

    /**
     * Recursively finds and deletes empty directories
     */
    public static void cleanupEmptyDirectories(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            return;
        }

        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) 
                    throws IOException {
                if (exc != null) throw exc;

                try (var files = Files.list(dir)) {
                    if (!files.findFirst().isPresent() && !dir.equals(directory)) {
                        Files.delete(dir);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}