# CCRM — Usage Guide

This guide explains how to configure, build, run, and use the **Campus Course & Records Manager (CCRM)** command-line application.

---

## 1. Prerequisites

Before running CCRM, ensure that you have:

- JDK 17 or higher
- PowerShell or Command Prompt
- Read/write permission for the project data directory
- The complete CCRM project source code

Verify Java:

```cmd
java --version
javac --version
```

---

## 2. Project Setup

From the project root, create the build directory:

```powershell
mkdir bin
```

The application stores its persistent records in the `data/` directory.

The main configuration file is:

```text
src/edu/ccrm/config/app.properties
```

A typical configuration is:

```properties
data.directory=./data
backup.directory=./data/backup
debug.mode=false
max.backup.files=5
```

---

## 3. Compile the Application

Compile all project packages with:

```powershell
javac -d bin src\edu\ccrm\CCRMApp.java src\edu\ccrm\cli\*.java src\edu\ccrm\config\*.java src\edu\ccrm\domain\*.java src\edu\ccrm\exception\*.java src\edu\ccrm\io\*.java src\edu\ccrm\service\*.java src\edu\ccrm\util\*.java
```

If compilation succeeds, the compiled `.class` files will be placed inside `bin/`.

---

## 4. Start CCRM

Run the main application:

```powershell
java -cp bin edu.ccrm.CCRMApp
```

The application opens through its command-line interface.

---

## 5. Student Management

Student records can be maintained through the student-management options.

### Add Student

Provide the requested details:

```text
Student ID: 1001
First Name: John
Last Name: Doe
Email: john.doe@example.com
```

### Other Student Operations

- **View Student** — Search for a student or display student records.
- **Update Student** — Modify an existing student's information.
- **Delete Student** — Remove a student while checking enrollment conditions.

---

## 6. Course Management

Course management is used to maintain the available courses and their semester offerings.

### Add Course

Example:

```text
Course Code: CS101
Course Name: Introduction to Programming
Capacity: 30
Semester: FALL_2025
```

### Other Course Operations

- **View Course** — Search by course code or display courses.
- **Update Course** — Change existing course details.
- **Delete Course** — Remove a course when it has no active enrollments.

---

## 7. Enrollment Management

Enrollment connects students with courses for a particular semester.

### Enroll a Student

Example:

```text
Student ID: 1001
Course Code: CS101
Semester: FALL_2025
```

### Available Enrollment Operations

- **Enroll Student** — Add a student to a course.
- **Drop Course** — Remove an existing enrollment.
- **View Enrollments** — View enrollments by student or course.
- **Update Grades** — Record or modify a student's grade.

The application applies enrollment rules such as duplicate-enrollment checks, course-capacity limits, and prerequisite validation.

---

## 8. Data Management

CCRM provides CSV-based data operations.

### Import Data

Loads records from CSV files into the application.

### Export Data

Saves application records in CSV format.

### Backup Data

Creates a timestamped backup of application data.

### Restore Backup

Recovers records from an available backup.

---

## 9. CSV File Formats

### `students.csv`

```csv
id,first_name,last_name,email,enrollment_date
1001,John,Doe,john.doe@example.com,2025-09-01
1002,Jane,Smith,jane.smith@example.com,2025-09-01
```

### `courses.csv`

```csv
code,name,capacity,instructor,semester
CS101,Introduction to Programming,30,Dr. Brown,FALL_2025
MATH201,Advanced Calculus,25,Dr. Smith,FALL_2025
```

### `enrollments.csv`

```csv
student_id,course_code,semester,grade
1001,CS101,FALL_2025,A
1002,MATH201,FALL_2025,B+
```

---

## 10. Data Validation

CCRM validates important input before accepting records.

### Student Data

- Student ID must follow the application's required format.
- A valid email format is required.

### Course Data

- Course codes must follow the application's expected format.
- Course capacity must be respected during enrollment.

### Grades

Supported grade values include:

```text
A, A-, B+, B, B-, C+, C, C-, D, F
```

---

## 11. Enrollment Rules

The application checks the following conditions:

1. Duplicate enrollments are not allowed.
2. A course cannot exceed its defined capacity.
3. Prerequisite validation is applied where required.
4. Course deletion is restricted when active enrollments exist.
5. Student deletion checks enrollment conditions.

---

## 12. Running with Assertions

Assertions can be enabled to verify application assumptions and business conditions.

Run CCRM with:

```powershell
java -ea -cp bin edu.ccrm.CCRMApp
```

Assertions can help detect invalid states during development and testing.

---

## 13. Debug Mode

For additional logging information, edit:

```text
src/edu/ccrm/config/app.properties
```

and set:

```properties
debug.mode=true
```

Restart the application after changing the configuration.

To return to normal operation:

```properties
debug.mode=false
```

---

## 14. Eclipse Usage

To use the project in Eclipse:

1. Open Eclipse IDE for Java Developers.
2. Select **File → Import → Existing Java Project**.
3. Select the CCRM project directory.
4. Check the Java build path if necessary.
5. Run the `CCRMApp` main class.

To enable assertions in Eclipse, add:

```text
-ea
```

to the VM arguments in the Run Configuration.

---

## 15. Cleaning the Project

To remove compiled files:

```powershell
Remove-Item -Path bin -Recurse -Force
```

After cleaning, repeat the compilation steps before running the application again.

---

## 16. Typical Workflow

A normal CCRM session can follow this sequence:

```text
Start Application
       │
       ▼
Configure / Load Data
       │
       ▼
Manage Students
       │
       ▼
Manage Courses
       │
       ▼
Enroll Students
       │
       ▼
Record / Update Grades
       │
       ▼
Export or Backup Data
       │
       ▼
Exit
```

---

## 17. Troubleshooting

### Java Command Not Found

Verify that JDK 17+ is installed and that Java is available through the system `PATH`.

### Compilation Errors

Check that:

- You are running the command from the project root.
- All source packages are present.
- The installed JDK version is compatible.

### File Permission Problems

Make sure the application has read/write access to:

```text
data/
data/backup/
```

### Invalid Input

Check the required student, course, email, and grade formats before submitting the record.

### Enrollment Failure

Check whether:

- The student is valid and active.
- The course exists.
- The course has available capacity.
- The student is not already enrolled.
- Required prerequisites are satisfied.

---

## Quick Start

For a basic setup and launch:

```powershell
mkdir bin
javac -d bin src\edu\ccrm\CCRMApp.java src\edu\ccrm\cli\*.java src\edu\ccrm\config\*.java src\edu\ccrm\domain\*.java src\edu\ccrm\exception\*.java src\edu\ccrm\io\*.java src\edu\ccrm\service\*.java src\edu\ccrm\util\*.java
java -cp bin edu.ccrm.CCRMApp
```

For assertions:

```powershell
java -ea -cp bin edu.ccrm.CCRMApp
```

