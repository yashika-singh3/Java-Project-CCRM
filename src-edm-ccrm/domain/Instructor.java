package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Instructor class representing a teacher in the CCRM system.
 * Demonstrates inheritance and encapsulation.
 */
public class Instructor extends Person {
    private String department;
    private String title; // e.g., "Professor", "Assistant Professor"
    private List<Course> assignedCourses;

    public Instructor(String id, String fullName, String email, String department, String title) {
        super(id, fullName, email);
        this.department = department;
        this.title = title;
        this.assignedCourses = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Instructor";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Returns an unmodifiable view of assigned courses
    public List<Course> getAssignedCourses() {
        return Collections.unmodifiableList(assignedCourses);
    }

    public void assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    public void unassignCourse(Course course) {
        assignedCourses.remove(course);
    }

    @Override
    public String toString() {
        return String.format("%s, Department: %s, Title: %s",
            super.toString(), department, title);
    }
}