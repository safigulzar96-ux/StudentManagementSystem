package ui;

import model.Enrollment;
import model.RankEntry;
import model.Student;
import service.EnrollmentService;
import service.StudentService;
import util.ErrorHandler;
import util.InputHelper;

import java.util.List;

/** Console menu for marks, averages and the class ranking report. */
public class ResultsMenu {

    private final InputHelper input;
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    public ResultsMenu(InputHelper input, EnrollmentService enrollmentService,
                       StudentService studentService) {
        this.input = input;
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
    }

    public void run() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Marks & Reports ---");
            System.out.println("1. Record / update marks");
            System.out.println("2. View a student's marks and average");
            System.out.println("3. Class ranking report");
            System.out.println("0. Back");
            int choice = input.readInt("Choose an option: ", 0, 3);

            try {
                switch (choice) {
                    case 1:
                        recordMarks();
                        break;
                    case 2:
                        viewStudentResults();
                        break;
                    case 3:
                        printRanking();
                        break;
                    default:
                        back = true;
                }
            } catch (Exception e) {
                ErrorHandler.handle(e);
            }
        }
    }

    private void recordMarks() throws Exception {
        int studentId = input.readInt("Student ID: ");
        int courseId = input.readInt("Course ID: ");
        double marks = input.readDouble("Marks (0-100): ", 0, 100);
        enrollmentService.recordMarks(studentId, courseId, marks);
        System.out.println("Marks saved successfully.");
    }

    private void viewStudentResults() throws Exception {
        int studentId = input.readInt("Student ID: ");
        Student student = studentService.getStudentById(studentId);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsOfStudent(studentId);

        System.out.println("Results for " + student.getName() + ":");
        if (enrollments.isEmpty()) {
            System.out.println("This student is not enrolled in any course.");
            return;
        }
        System.out.printf("%-12s %-30s %-8s%n", "Code", "Course Name", "Marks");
        System.out.println("-".repeat(52));
        for (Enrollment enrollment : enrollments) {
            String marks = enrollment.getMarks() == null
                    ? "Not recorded" : String.format("%.2f", enrollment.getMarks());
            System.out.printf("%-12s %-30s %-8s%n",
                    enrollment.getCourseCode(), enrollment.getCourseName(), marks);
        }
        Double average = enrollmentService.calculateAverage(enrollments);
        System.out.println("Average marks: "
                + (average == null ? "No marks recorded yet" : String.format("%.2f", average)));
    }

    private void printRanking() throws Exception {
        List<RankEntry> ranking = enrollmentService.getRanking();
        if (ranking.isEmpty()) {
            System.out.println("No marks have been recorded yet, so there is no ranking.");
            return;
        }
        System.out.println("\n=== Class Ranking (by average marks) ===");
        System.out.printf("%-6s %-6s %-25s %-8s %-8s%n", "Rank", "ID", "Name", "Courses", "Average");
        System.out.println("-".repeat(58));
        for (RankEntry row : ranking) {
            System.out.printf("%-6d %-6d %-25s %-8d %-8.2f%n", row.getRank(), row.getStudentId(),
                    row.getStudentName(), row.getCoursesMarked(), row.getAverage());
        }
    }
}
