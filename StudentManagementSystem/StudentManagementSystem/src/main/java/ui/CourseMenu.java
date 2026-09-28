package ui;

import model.Course;
import service.CourseService;
import util.ErrorHandler;
import util.InputHelper;

import java.util.List;

/** Console menu for courses. */
public class CourseMenu {

    private final InputHelper input;
    private final CourseService courseService;

    public CourseMenu(InputHelper input, CourseService courseService) {
        this.input = input;
        this.courseService = courseService;
    }

    public void run() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Add course");
            System.out.println("2. View all courses");
            System.out.println("3. Delete course");
            System.out.println("0. Back");
            int choice = input.readInt("Choose an option: ", 0, 3);

            try {
                switch (choice) {
                    case 1:
                        addCourse();
                        break;
                    case 2:
                        printCourses(courseService.getAllCourses());
                        break;
                    case 3:
                        deleteCourse();
                        break;
                    default:
                        back = true;
                }
            } catch (Exception e) {
                ErrorHandler.handle(e);
            }
        }
    }

    private void addCourse() throws Exception {
        String code = input.readRequired("Course code (e.g. JAVA101): ");
        String name = input.readRequired("Course name: ");
        int credits = input.readInt("Credit hours (1-10): ", 1, 10);
        int id = courseService.addCourse(code, name, credits);
        System.out.println("Course added successfully. New course ID: " + id);
    }

    private void deleteCourse() throws Exception {
        int id = input.readInt("Course ID to delete: ");
        Course course = courseService.getCourseById(id);
        if (input.confirm("Delete " + course + "? Its enrollments and marks will also be deleted")) {
            courseService.deleteCourse(id);
            System.out.println("Course deleted.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private void printCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        System.out.printf("%-6s %-12s %-30s %-6s%n", "ID", "Code", "Course Name", "Credits");
        System.out.println("-".repeat(58));
        for (Course course : courses) {
            course.display();
        }
    }
}
