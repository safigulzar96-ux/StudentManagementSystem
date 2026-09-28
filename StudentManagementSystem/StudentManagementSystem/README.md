# Student Management System

A menu-driven, terminal-based Java application for managing students, courses, enrollments and marks.
Data is stored in a MySQL database using JDBC, so it is still there when the program is closed.

## Features
- Add, view, update and delete student records
- Search students by ID or by name
- Add, view and delete courses
- Enroll and unenroll students in courses
- List all students in a course
- Record and update marks for each student in each course
- View a student's marks with the computed average
- Class ranking report (sorted by average marks)
- Input validation and clear error messages

## Technologies
- Java 11 or higher (core Java, no frameworks)
- MySQL 8 with the MySQL Connector/J JDBC driver (8.4.0)
- Maven (optional) for building
- Git and GitHub for version control

## Project structure
```
src/main/java/
  model/      Person (abstract), Student, Course, Enrollment, RankEntry, Displayable (interface)
  dao/        StudentDAO, CourseDAO, EnrollmentDAO  (all SQL with PreparedStatement)
  service/    StudentService, CourseService, EnrollmentService  (business rules)
  ui/         StudentMenu, CourseMenu, EnrollmentMenu, ResultsMenu  (console menus)
  util/       DBConnection, InputHelper, ErrorHandler
  exception/  RecordNotFoundException, DuplicateRecordException
  main/       Main  (starts the program)
schema.sql        Creates the database and tables
sample_data.sql   Optional demo data
```

## Database setup
1. Install and start MySQL.
2. Run the schema script:
   ```
   mysql -u root -p < schema.sql
   ```
3. (Optional) load demo data:
   ```
   mysql -u root -p < sample_data.sql
   ```
4. Copy `db.properties.example` to `db.properties` and enter your MySQL username and password.
   (You can also set the environment variables `DB_URL`, `DB_USER` and `DB_PASSWORD`.)

## How to run

### Option A - Maven
```
mvn compile exec:java
```

### Option B - javac / java
Download `mysql-connector-j-8.4.0.jar` and put it in a `lib/` folder, then:

Windows:
```
javac -cp "lib/*" -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object FullName)
java -cp "out;lib/*" main.Main
```
Linux / macOS:
```
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:lib/*" main.Main
```
Run the program from the project root so it can find `db.properties`.

## Screenshots
Add your screenshots in `docs/screenshots/` and link them here:

![Main menu](docs/screenshots/main-menu.png)
![Student list](docs/screenshots/student-list.png)
![Ranking report](docs/screenshots/ranking-report.png)

## Known limitations
- No login system (admin / student modes were not built).
- No attendance tracking.
- Reports are shown only on the console (no export to .txt or .csv).
- Marks are stored per course as a single number (no separate exams or assignments).
