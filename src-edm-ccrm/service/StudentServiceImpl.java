package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementation of StudentService interface.
 * Demonstrates Stream API, lambda expressions, and thread-safe collections.
 */
public class StudentServiceImpl implements StudentService {
    private final Map<String, Student> students = new ConcurrentHashMap<>();

    @Override
    public Student findById(String id) {
        return students.get(id);
    }

    @Override
    public Student save(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public void delete(String id) {
        students.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return students.containsKey(id);
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    @Override
    public List<Student> findByPredicate(Predicate<Student> predicate) {
        return students.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return students.size();
    }

    @Override
    public Student findByRegNo(String regNo) {
        return students.values().stream()
            .filter(s -> s.getRegNo().equals(regNo))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Student> findByDepartment(String department) {
        return findByPredicate(s -> s.isActive() && 
            s.getEnrolledCourses().stream()
                .anyMatch(e -> e.getCourse().getDepartment().equals(department)));
    }

    @Override
    public double calculateAverageGpa() {
        return students.values().stream()
            .filter(Student::isActive)
            .mapToDouble(Student::getGpa)
            .average()
            .orElse(0.0);
    }

    @Override
    public List<Student> findTopPerformers(int limit) {
        return students.values().stream()
            .filter(Student::isActive)
            .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    @Override
    public void deactivateStudent(String id) {
        Student student = findById(id);
        if (student != null) {
            student.setActive(false);
            save(student);
        }
    }
}