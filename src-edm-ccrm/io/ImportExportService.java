package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.service.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for importing and exporting data using NIO.2.
 * Demonstrates modern Java I/O operations and Stream API usage.
 */
public class ImportExportService implements DataPersistenceService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private static final String CSV_DELIMITER = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public ImportExportService(
            StudentService studentService,
            CourseService courseService,
            EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @Override
    public void exportData(Path directory) throws IOException {
        // Ensure directory exists
        Files.createDirectories(directory);

        // Export students
        Path studentsFile = directory.resolve("students.csv");
        List<String> studentLines = studentService.findAll().stream()
            .map(this::studentToCsv)
            .collect(Collectors.toList());
        Files.write(studentsFile, studentLines, StandardOpenOption.CREATE, 
            StandardOpenOption.TRUNCATE_EXISTING);

        // Export courses
        Path coursesFile = directory.resolve("courses.csv");
        List<String> courseLines = courseService.findAll().stream()
            .map(this::courseToCsv)
            .collect(Collectors.toList());
        Files.write(coursesFile, courseLines, StandardOpenOption.CREATE, 
            StandardOpenOption.TRUNCATE_EXISTING);

        // Export enrollments
        Path enrollmentsFile = directory.resolve("enrollments.csv");
        List<String> enrollmentLines = enrollmentService.findAll().stream()
            .map(this::enrollmentToCsv)
            .collect(Collectors.toList());
        Files.write(enrollmentsFile, enrollmentLines, StandardOpenOption.CREATE, 
            StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void importData(Path directory) throws IOException {
        // Import students
        Path studentsFile = directory.resolve("students.csv");
        if (Files.exists(studentsFile)) {
            try (Stream<String> lines = Files.lines(studentsFile)) {
                lines.skip(1) // Skip header
                    .map(this::csvToStudent)
                    .forEach(studentService::save);
            }
        }

        // Import courses
        Path coursesFile = directory.resolve("courses.csv");
        if (Files.exists(coursesFile)) {
            try (Stream<String> lines = Files.lines(coursesFile)) {
                lines.skip(1) // Skip header
                    .map(this::csvToCourse)
                    .forEach(courseService::save);
            }
        }

        // Import enrollments
        Path enrollmentsFile = directory.resolve("enrollments.csv");
        if (Files.exists(enrollmentsFile)) {
            try (Stream<String> lines = Files.lines(enrollmentsFile)) {
                lines.skip(1) // Skip header
                    .forEach(line -> {
                        try {
                            processEnrollmentLine(line);
                        } catch (Exception e) {
                            // Log error and continue with next line
                            System.err.println("Error processing enrollment: " + e.getMessage());
                        }
                    });
            }
        }
    }

    private String studentToCsv(Student student) {
        return String.join(CSV_DELIMITER,
            student.getId(),
            student.getRegNo(),
            student.getFullName(),
            student.getEmail(),
            student.getEnrollmentDate().format(DATE_FORMATTER),
            String.valueOf(student.isActive()));
    }

    private String courseToCsv(Course course) {
        return String.join(CSV_DELIMITER,
            course.getCode(),
            course.getTitle(),
            String.valueOf(course.getCredits()),
            course.getDepartment(),
            course.getSemester().name(),
            course.getInstructor() != null ? course.getInstructor().getId() : "",
            String.valueOf(course.isActive()));
    }

    private String enrollmentToCsv(Enrollment enrollment) {
        return String.join(CSV_DELIMITER,
            enrollment.getStudent().getId(),
            enrollment.getCourse().getCode(),
            enrollment.getEnrollmentDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            enrollment.getStatus().name(),
            enrollment.getGrade() != null ? enrollment.getGrade().name() : "");
    }

    private Student csvToStudent(String line) {
        String[] parts = line.split(CSV_DELIMITER);
        return new Student(
            parts[0], // id
            parts[2], // fullName
            parts[3], // email
            parts[1]  // regNo
        );
    }

    private Course csvToCourse(String line) {
        String[] parts = line.split(CSV_DELIMITER);
        return new Course.Builder(parts[0]) // code
            .title(parts[1])
            .credits(Integer.parseInt(parts[2]))
            .department(parts[3])
            .semester(Semester.valueOf(parts[4]))
            .build();
    }

    private void processEnrollmentLine(String line) throws Exception {
        String[] parts = line.split(CSV_DELIMITER);
        Student student = studentService.findById(parts[0]);
        Course course = courseService.findById(parts[1]);
        
        if (student != null && course != null) {
            Enrollment enrollment = enrollmentService.enroll(student, course);
            if (parts.length > 4 && !parts[4].isEmpty()) {
                enrollment.setGrade(Grade.valueOf(parts[4]));
            }
        }
    }

    @Override
    public void backup(Path backupDirectory) throws IOException {
        // Create timestamp-based backup folder
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path backupPath = backupDirectory.resolve("backup_" + timestamp);
        Files.createDirectories(backupPath);

        // Export data to backup location
        exportData(backupPath);
    }

    @Override
    public long getBackupSize(Path backupDirectory) throws IOException {
        // Use walk to recursively calculate size
        try (Stream<Path> walk = Files.walk(backupDirectory)) {
            return walk.filter(Files::isRegularFile)
                      .mapToLong(path -> {
                          try {
                              return Files.size(path);
                          } catch (IOException e) {
                              return 0L;
                          }
                      })
                      .sum();
        }
    }
}