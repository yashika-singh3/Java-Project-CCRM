package edu.ccrm.domain;

/**
 * Course class representing a course in the CCRM system.
 * Demonstrates Builder pattern and immutable course code.
 */
public class Course {
    private final String code;
    private String title;
    private int credits;
    private Instructor instructor;
    private String department;
    private Semester semester;
    private boolean active;

    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.department = builder.department;
        this.semester = builder.semester;
        this.active = true;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("Course[code=%s, title=%s, credits=%d, instructor=%s, department=%s, semester=%s]",
            code, title, credits, instructor != null ? instructor.getFullName() : "TBA",
            department, semester);
    }

    // Builder class
    public static class Builder {
        // Required parameters
        private final String code;

        // Optional parameters
        private String title;
        private int credits;
        private Instructor instructor;
        private String department;
        private Semester semester;

        public Builder(String code) {
            this.code = code;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder instructor(Instructor instructor) {
            this.instructor = instructor;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }
}