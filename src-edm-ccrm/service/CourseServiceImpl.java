package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementation of CourseService interface.
 * Demonstrates Stream API and thread-safe collections.
 */
public class CourseServiceImpl implements CourseService {
    private final Map<String, Course> courses = new ConcurrentHashMap<>();

    @Override
    public Course findById(String code) {
        return courses.get(code);
    }

    @Override
    public Course save(Course course) {
        courses.put(course.getCode(), course);
        return course;
    }

    @Override
    public void delete(String code) {
        courses.remove(code);
    }

    @Override
    public boolean exists(String code) {
        return courses.containsKey(code);
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public List<Course> findByPredicate(Predicate<Course> predicate) {
        return courses.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return courses.size();
    }

    @Override
    public List<Course> findByInstructor(Instructor instructor) {
        return findByPredicate(course -> 
            course.isActive() && 
            course.getInstructor() != null && 
            course.getInstructor().equals(instructor));
    }

    @Override
    public List<Course> findByDepartment(String department) {
        return findByPredicate(course -> 
            course.isActive() && 
            course.getDepartment().equals(department));
    }

    @Override
    public List<Course> findBySemester(Semester semester) {
        return findByPredicate(course -> 
            course.isActive() && 
            course.getSemester() == semester);
    }

    @Override
    public void assignInstructor(String courseCode, Instructor instructor) {
        Course course = findById(courseCode);
        if (course != null) {
            course.setInstructor(instructor);
            instructor.assignCourse(course);
            save(course);
        }
    }

    @Override
    public void deactivateCourse(String code) {
        Course course = findById(code);
        if (course != null) {
            course.setActive(false);
            save(course);
        }
    }
}