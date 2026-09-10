package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementation of EnrollmentService interface.
 * Demonstrates complex business logic, exception handling, and Stream API usage.
 */
public class EnrollmentServiceImpl implements EnrollmentService {
    private final Map<String, Enrollment> enrollments = new ConcurrentHashMap<>();
    private static final int MAX_CREDITS_PER_SEMESTER = 18;

    private String generateEnrollmentId(Student student, Course course) {
        return student.getId() + "-" + course.getCode();
    }

    @Override
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments.values());
    }

    @Override
    public List<Enrollment> findByPredicate(Predicate<Enrollment> predicate) {
        return enrollments.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return enrollments.size();
    }

    @Override
    public Enrollment enroll(Student student, Course course) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        
        // Check for duplicate enrollment
        String enrollmentId = generateEnrollmentId(student, course);
        if (enrollments.containsKey(enrollmentId)) {
            throw new DuplicateEnrollmentException(
                "Student is already enrolled in this course");
        }

        // Check credit limit
        int currentCredits = getCurrentCredits(student, course.getSemester());
        if (currentCredits + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException(
                String.format("Enrolling in this course would exceed the maximum of %d credits per semester", 
                    MAX_CREDITS_PER_SEMESTER));
        }

        // Create and save enrollment
        Enrollment enrollment = new Enrollment(student, course);
        enrollments.put(enrollmentId, enrollment);
        student.addEnrollment(enrollment);
        return enrollment;
    }

    @Override
    public void withdraw(Student student, Course course) {
        String enrollmentId = generateEnrollmentId(student, course);
        Enrollment enrollment = enrollments.get(enrollmentId);
        
        if (enrollment != null && enrollment.getStatus() == Enrollment.EnrollmentStatus.ENROLLED) {
            enrollment.withdraw();
            student.removeEnrollment(enrollment);
        }
    }

    @Override
    public void assignGrade(Student student, Course course, Grade grade) {
        String enrollmentId = generateEnrollmentId(student, course);
        Enrollment enrollment = enrollments.get(enrollmentId);
        
        if (enrollment != null) {
            enrollment.setGrade(grade);
        }
    }

    @Override
    public List<Enrollment> findByStudent(Student student) {
        return findByPredicate(e -> e.getStudent().equals(student));
    }

    @Override
    public List<Enrollment> findByCourse(Course course) {
        return findByPredicate(e -> e.getCourse().equals(course));
    }

    @Override
    public List<Enrollment> findByStudentAndSemester(Student student, Semester semester) {
        return findByPredicate(e -> 
            e.getStudent().equals(student) && 
            e.getCourse().getSemester() == semester);
    }

    @Override
    public double calculateGpa(Student student, Semester semester) {
        List<Enrollment> semesterEnrollments = findByStudentAndSemester(student, semester);
        
        if (semesterEnrollments.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : semesterEnrollments) {
            if (enrollment.getGrade() != null) {
                int credits = enrollment.getCourse().getCredits();
                totalPoints += enrollment.getGrade().getPoints() * credits;
                totalCredits += credits;
            }
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    @Override
    public boolean hasPassedPrerequisites(Student student, Course course) {
        // This is a placeholder for prerequisite checking logic
        // In a real implementation, would check course prerequisites and student's grades
        return true;
    }

    @Override
    public int getCurrentCredits(Student student, Semester semester) {
        return findByStudentAndSemester(student, semester).stream()
            .filter(e -> e.getStatus() == Enrollment.EnrollmentStatus.ENROLLED)
            .mapToInt(e -> e.getCourse().getCredits())
            .sum();
    }
}