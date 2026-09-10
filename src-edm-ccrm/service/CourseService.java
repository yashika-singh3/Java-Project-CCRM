package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import java.util.List;

/**
 * Interface defining course management operations.
 * Demonstrates interface inheritance and specialized operations.
 */
public interface CourseService extends Persistable<Course, String>, Searchable<Course> {
    List<Course> findByInstructor(Instructor instructor);
    List<Course> findByDepartment(String department);
    List<Course> findBySemester(Semester semester);
    void assignInstructor(String courseCode, Instructor instructor);
    void deactivateCourse(String code);
}