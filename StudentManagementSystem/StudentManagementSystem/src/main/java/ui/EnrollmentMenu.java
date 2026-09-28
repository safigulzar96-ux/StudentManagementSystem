package ui;

import model.Course;
import model.Student;
import service.CourseService;
import service.EnrollmentService;
import util.ErrorHandler;
import util.InputHelper;

import java.util.List;

/** Console menu for enrolling and unenrolling students. */
public class EnrollmentMenu {

    private final InputHelper input;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;

    public EnrollmentMenu(InputHelper input, EnrollmentService enrollmentService,
                          CourseService courseService) {
        this.input = input;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
    }

    public void run() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Enrollment ---");
            System.out.println("1. Enroll student in a course");
            System.out.println("2. Unenroll student from a course");
            System.out.println("3. List students in a course");
            System.out.println("0. Back");
            int choice = input.readInt("Choose an option: ", 0, 3);

            try {
                switch (choice) {
                    case 1:
                        enroll();
                        break;
                    case 2:
                        unenroll();
                        break;
                    case 3:
                        listStudentsInCourse();
                        break;
                    default:
                        back = true;
                }
            } catch (Exception e) {
                ErrorHandler.handle(e);
            }
        }
    }

    private void enroll() throws Exception {
        int studentId = input.readInt("Student ID: ");
        int courseId = input.readInt("Course ID: ");
        enrollmentService.enroll(studentId, courseId);
        System.out.println("Student enrolled successfully.");
    }

    private void unenroll() throws Exception {
        int studentId = input.readInt("Student ID: ");
        int courseId = input.readInt("Course ID: ");
        enrollmentService.unenroll(studentId, courseId);
        System.out.println("Student unenrolled successfully.");
    }

    private void listStudentsInCourse() throws Exception {
        int courseId = input.readInt("Course ID: ");
        Course course = courseService.getCourseById(courseId);
        List<Student> students = enrollmentService.getStudentsInCourse(courseId);
        System.out.println("Students in " + course + ":");
        if (students.isEmpty()) {
            System.out.println("No students are enrolled in this course.");
            return;
        }
        System.out.printf("%-6s %-25s %-28s %-15s%n", "ID", "Name", "Email", "Phone");
        System.out.println("-".repeat(76));
        for (Student student : students) {
            student.display();
        }
    }
}
