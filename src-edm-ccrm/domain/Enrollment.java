package edu.ccrm.domain;

import java.time.LocalDateTime;

/**
 * Enrollment class representing a student's enrollment in a course.
 * Demonstrates composition and relationship between domain objects.
 */
public class Enrollment {
    private final Student student;
    private final Course course;
    private Grade grade;
    private final LocalDateTime enrollmentDate;
    private LocalDateTime withdrawalDate;
    private EnrollmentStatus status;

    public enum EnrollmentStatus {
        ENROLLED,
        WITHDRAWN,
        COMPLETED
    }

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
        this.status = EnrollmentStatus.ENROLLED;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
        if (grade != null) {
            this.status = EnrollmentStatus.COMPLETED;
        }
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public LocalDateTime getWithdrawalDate() {
        return withdrawalDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void withdraw() {
        this.status = EnrollmentStatus.WITHDRAWN;
        this.withdrawalDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Enrollment[student=%s, course=%s, status=%s, grade=%s]",
            student.getRegNo(), course.getCode(), status,
            grade != null ? grade.name() : "Not Graded");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Enrollment)) return false;
        Enrollment other = (Enrollment) obj;
        return student.equals(other.student) && course.equals(other.course);
    }

    @Override
    public int hashCode() {
        return 31 * student.hashCode() + course.hashCode();
    }
}