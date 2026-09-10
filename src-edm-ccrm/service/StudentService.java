package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.util.List;

/**
 * Interface defining student management operations.
 * Demonstrates interface inheritance and business operations.
 */
public interface StudentService extends Persistable<Student, String>, Searchable<Student> {
    Student findByRegNo(String regNo);
    List<Student> findByDepartment(String department);
    double calculateAverageGpa();
    List<Student> findTopPerformers(int limit);
    void deactivateStudent(String id);
}