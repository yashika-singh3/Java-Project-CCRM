# CCRM — Campus Course & Records Manager

A Java-based command-line application for managing students, courses, enrollments, grades, and academic records. The project combines practical Java development with object-oriented design, file handling, validation, and data-processing concepts.

---

## Project Snapshot

| Area | Details |
|---|---|
| Language | Java |
| Recommended JDK | 17 or higher |
| Interface | Command Line |
| Persistence | CSV files |
| File API | Java NIO.2 |
| Main Package | `edu.ccrm` |

---

## What the Application Does

CCRM is designed around the everyday academic workflow of an educational institution.

### Student Management
- Add, update, view, and delete student records
- Validate student information
- Track active student status

### Course & Enrollment Management
- Maintain a course catalog
- Control course capacity
- Create semester-based course offerings
- Enroll students in courses
- Track grades and academic records

### Data Handling
- Store records in CSV files
- Import and export data
- Create automated backups
- Handle invalid or inconsistent data safely

---

## Architecture

The project follows a layered structure so that the user interface, business rules, data handling, and domain models remain separated.

```text
ccrm-java-project/
│
├── data/                 # CSV records and application data
├── screenshots/          # Setup and application screenshots
└── src/
    └── edu/
        └── ccrm/
            ├── domain/       # Student, Course, Person, etc.
            ├── service/      # Business and application logic
            ├── io/           # File and persistence operations
            ├── cli/          # Command-line interface
            ├── config/       # Application configuration
            ├── util/         # Utility and helper classes
            └── exception/    # Custom exception classes
```

### Design Approach

The implementation demonstrates several common Java design concepts:

- **Inheritance:** `Person` → `Student` / `Instructor`
- **Encapsulation:** Data and related behavior are kept inside classes
- **Interfaces:** Service contracts such as `Persistable` and `Searchable`
- **Polymorphism:** Service implementations can work through common contracts
- **Singleton:** `AppConfig`
- **Builder:** Course creation
- **Service Layer:** Separates business logic from the CLI
- **DAO-style separation:** Keeps persistence operations independent

---

## Main Java Concepts Demonstrated

| Concept | Example Location |
|---|---|
| Abstract Classes | `src/edu/ccrm/domain/Person.java` |
| Inheritance | `Student.java`, `Instructor.java` |
| Interfaces | `Persistable.java`, `Searchable.java` |
| Collections | `StudentServiceImpl.java` |
| Stream API | `EnrollmentServiceImpl.java` |
| File I/O / NIO.2 | `ImportExportService.java` |
| Exception Handling | `src/edu/ccrm/exception/` |
| Generics | `Persistable.java` |
| Lambda Expressions | `CourseComparator.java` |
| Date/Time API | `DateTimeUtil.java` |
| Assertions | Enrollment and validation logic |

---

## Requirements

Before running the project, make sure you have:

- JDK 17 or later
- PowerShell or Command Prompt
- Read/write access to the project's data directory
- The complete project source tree

Verify Java installation with:

```cmd
java --version
javac --version
```

---

## Run the Project

### 1. Open the Project Directory

Open PowerShell in the root folder of the project.

### 2. Create the Build Directory

```powershell
mkdir bin
```

### 3. Compile

```powershell
javac -d bin src\edu\ccrm\CCRMApp.java src\edu\ccrm\cli\*.java src\edu\ccrm\config\*.java src\edu\ccrm\domain\*.java src\edu\ccrm\exception\*.java src\edu\ccrm\io\*.java src\edu\ccrm\service\*.java src\edu\ccrm\util\*.java
```

### 4. Start CCRM

```powershell
java -cp bin edu.ccrm.CCRMApp
```

### 5. Clean Compiled Files

```powershell
Remove-Item -Path bin -Recurse -Force
```

---

## First-Time Configuration

The application uses:

```text
src/edu/ccrm/config/app.properties
```

If required, create the data directory before starting:

```bash
mkdir -p data
```

During initial setup, the application can create required directories, generate initial data files, and display the default administrator credentials.

---

## Assertions

Assertions are used to check important assumptions and business conditions during execution.

For example, enrollment validation can check that:

```java
assert student != null : "Student cannot be null";
assert course != null : "Course cannot be null";
assert student.isActive() : "Student must be active";
```

After processing, an assertion can verify that enrollment succeeded:

```java
assert student.getEnrolledCourses().contains(course)
       : "Enrollment failed";
```

### Run with Assertions Enabled

```cmd
java -ea -cp bin edu.ccrm.CCRMApp
```

In Eclipse, add:

```text
-ea
```

to the VM arguments of the Run Configuration.

---

## Java Platform Background

The project is built using Java SE concepts and demonstrates how Java has evolved as a platform.

### Selected Milestones

- **1995 — Java 1.0:** Initial release and the "Write Once, Run Anywhere" approach
- **1998 — Java 2:** Introduction of Java platform editions
- **2004 — Java 5:** Generics, annotations, enums, enhanced `for` loop, and autoboxing
- **2007 — Java 6:** Performance improvements and JDBC enhancements
- **2011 — Java 7:** Try-with-resources, diamond operator, multi-catch, and NIO.2
- **2014 — Java 8:** Lambda expressions, Stream API, Optional, and the modern Date/Time API
- **2018 — Java 11 (LTS):** HTTP Client API and additional language/runtime improvements
- **2021 — Java 17 (LTS):** Sealed classes and stronger JDK encapsulation
- **2023 — Java 21 (LTS):** Virtual threads, record patterns, and sequenced collections

---

## Java Editions

### Java SE
The core Java platform used for desktop applications, command-line programs, standard libraries, collections, I/O, concurrency, and security features.

### Java EE
Designed for enterprise-scale and distributed applications, including web technologies, persistence, REST, WebSocket, and dependency injection.

### Java ME
Designed for devices with limited resources, including embedded and mobile environments.

---

## How Java Runs CCRM

```text
Java Source (.java)
       │
       ▼
    javac
       │
       ▼
Bytecode (.class)
       │
       ▼
   Class Loader
       │
       ▼
Bytecode Verification
       │
       ▼
       JVM
   ┌───┴───────────────┐
   │ Execution Engine  │
   │ Interpreter       │
   │ JIT Compiler      │
   └───┬───────────────┘
       │
       ▼
Running Application
```

The **JDK** provides development tools such as `javac`, while the **JVM** loads and executes Java bytecode. The runtime also provides memory areas and automatic garbage collection.

---

## Eclipse Setup

To work with the project in Eclipse:

1. Install **Eclipse IDE for Java Developers**.
2. Open Eclipse.
3. Select **File → Import → Existing Java Project**.
4. Choose the `ccrm-java-project` directory.
5. Check the Java build path if required.
6. Run `CCRMApp`.

Screenshots included with the project can be found in:

```text
screenshots/
```

---

## Learning Map

This project can also be used as a practical reference for Java coursework:

```text
OOP
├── Abstraction
├── Encapsulation
├── Inheritance
└── Polymorphism

Modern Java
├── Generics
├── Lambda Expressions
├── Stream API
└── Date/Time API

Application Development
├── Collections
├── File I/O
├── Exception Handling
├── Assertions
└── Configuration
```

---

## Quick Start

For the shortest setup path:

```powershell
mkdir bin
javac -d bin src\edu\ccrm\CCRMApp.java src\edu\ccrm\cli\*.java src\edu\ccrm\config\*.java src\edu\ccrm\domain\*.java src\edu\ccrm\exception\*.java src\edu\ccrm\io\*.java src\edu\ccrm\service\*.java src\edu\ccrm\util\*.java
java -cp bin edu.ccrm.CCRMApp
```

To enable assertions:

```powershell
java -ea -cp bin edu.ccrm.CCRMApp
```

---

## Additional Documentation

For detailed application operation instructions, refer to:

```text
USAGE.md
```

---

## Project Goal

CCRM brings together Java fundamentals and practical application development in one academic project. Its structure makes it useful not only as a course project, but also as a reference for understanding how Java concepts fit together inside a real application.
