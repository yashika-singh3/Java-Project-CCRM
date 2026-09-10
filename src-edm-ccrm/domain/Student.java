package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Student class representing a student in the CCRM system.
 * Demonstrates inheritance, encapsulation, and composition.
 */
public class Student extends Person {
    private String regNo;
    private LocalDate enrollmentDate;
    private List<Enrollment> enrolledCourses;
    private double gpa;

    public Student(String id, String fullName, String email, String regNo) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.enrollmentDate = LocalDate.now();
        this.enrolledCourses = new ArrayList<>();
        this.gpa = 0.0;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getRegNo() {
        return regNo;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    // Returns an unmodifiable view of the enrollments
    public List<Enrollment> getEnrolledCourses() {
        return Collections.unmodifiableList(enrolledCourses);
    }

    public void addEnrollment(Enrollment enrollment) {
        enrolledCourses.add(enrollment);
        updateGPA();
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrolledCourses.remove(enrollment);
        updateGPA();
    }

    public double getGpa() {
        return gpa;
    }

    private void updateGPA() {
        if (enrolledCourses.isEmpty()) {
            gpa = 0.0;
            return;
        }

        double totalPoints = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrolledCourses) {
            if (enrollment.getGrade() != null) {
                totalPoints += enrollment.getGrade().getPoints() * enrollment.getCourse().getCredits();
                totalCredits += enrollment.getCourse().getCredits();
            }
        }

        gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    @Override
    public String toString() {
        return String.format("%s, RegNo: %s, GPA: %.2f, Enrolled: %s",
            super.toString(), regNo, gpa, enrollmentDate);
    }
}