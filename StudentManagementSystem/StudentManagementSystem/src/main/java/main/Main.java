package main;

import service.CourseService;
import service.EnrollmentService;
import service.StudentService;
import ui.CourseMenu;
import ui.EnrollmentMenu;
import ui.ResultsMenu;
import ui.StudentMenu;
import util.DBConnection;
import util.InputHelper;

import java.sql.Connection;
import java.sql.SQLException;

/** Starting point of the Student Management System. */
public class Main {

    public static void main(String[] args) {
        if (!databaseIsReachable()) {
            return;
        }

        InputHelper input = new InputHelper();
        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();
        EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService);

        StudentMenu studentMenu = new StudentMenu(input, studentService);
        CourseMenu courseMenu = new CourseMenu(input, courseService);
        EnrollmentMenu enrollmentMenu = new EnrollmentMenu(input, enrollmentService, courseService);
        ResultsMenu resultsMenu = new ResultsMenu(input, enrollmentService, studentService);

        System.out.println("=====================================");
        System.out.println("   STUDENT MANAGEMENT SYSTEM");
        System.out.println("=====================================");

        try {
            boolean running = true;
            while (running) {
                System.out.println("\n--- Main Menu ---");
                System.out.println("1. Student Management");
                System.out.println("2. Course Management");
                System.out.println("3. Enrollment");
                System.out.println("4. Marks & Reports");
                System.out.println("0. Exit");
                int choice = input.readInt("Choose an option: ", 0, 4);

                switch (choice) {
                    case 1:
                        studentMenu.run();
                        break;
                    case 2:
                        courseMenu.run();
                        break;
                    case 3:
                        enrollmentMenu.run();
                        break;
                    case 4:
                        resultsMenu.run();
                        break;
                    default:
                        running = false;
                }
            }
            System.out.println("Goodbye!");
        } catch (IllegalStateException e) {
            System.out.println("\nInput closed. Goodbye!");
        }
    }

    /** Checks the database settings before showing any menu. */
    private static boolean databaseIsReachable() {
        try (Connection connection = DBConnection.getConnection()) {
            return connection.isValid(3);
        } catch (SQLException e) {
            System.out.println("Could not connect to the database: " + e.getMessage());
            System.out.println("Check db.properties and make sure MySQL is running and schema.sql was run.");
            return false;
        }
    }
}
