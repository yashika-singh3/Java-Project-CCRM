package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import java.util.List;

/**
 * Interface defining enrollment management operations.
 * Demonstrates business logic and exception handling requirements.
 */
public interface EnrollmentService extends Searchable<Enrollment> {
    Enrollment enroll(Student student, Course course) 
        throws DuplicateEnrollmentException, MaxCreditLimitExceededException;
    void withdraw(Student student, Course course);
    void assignGrade(Student student, Course course, Grade grade);
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findByCourse(Course course);
    List<Enrollment> findByStudentAndSemester(Student student, Semester semester);
    double calculateGpa(Student student, Semester semester);
    boolean hasPassedPrerequisites(Student student, Course course);
    int getCurrentCredits(Student student, Semester semester);
}