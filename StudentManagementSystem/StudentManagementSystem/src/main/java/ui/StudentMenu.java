package ui;

import model.Student;
import service.StudentService;
import util.ErrorHandler;
import util.InputHelper;

import java.util.List;

/** Console menu for student records. */
public class StudentMenu {

    private final InputHelper input;
    private final StudentService studentService;

    public StudentMenu(InputHelper input, StudentService studentService) {
        this.input = input;
        this.studentService = studentService;
    }

    public void run() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add student");
            System.out.println("2. View all students");
            System.out.println("3. Search student by ID");
            System.out.println("4. Search students by name");
            System.out.println("5. Update student");
            System.out.println("6. Delete student");
            System.out.println("0. Back");
            int choice = input.readInt("Choose an option: ", 0, 6);

            try {
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        printStudents(studentService.getAllStudents());
                        break;
                    case 3:
                        searchById();
                        break;
                    case 4:
                        searchByName();
                        break;
                    case 5:
                        updateStudent();
                        break;
                    case 6:
                        deleteStudent();
                        break;
                    default:
                        back = true;
                }
            } catch (Exception e) {
                ErrorHandler.handle(e);
            }
        }
    }

    private void addStudent() throws Exception {
        String name = input.readRequired("Name: ");
        String email = input.readRequired("Email: ");
        String phone = input.readLine("Phone (optional): ");
        int id = studentService.addStudent(name, email, phone);
        System.out.println("Student added successfully. New student ID: " + id);
    }

    private void searchById() throws Exception {
        int id = input.readInt("Student ID: ");
        Student student = studentService.getStudentById(id);
        printStudents(List.of(student));
    }

    private void searchByName() throws Exception {
        String keyword = input.readRequired("Name (or part of it): ");
        printStudents(studentService.searchByName(keyword));
    }

    private void updateStudent() throws Exception {
        int id = input.readInt("Student ID to update: ");
        Student current = studentService.getStudentById(id);
        System.out.println("Current details:");
        printStudents(List.of(current));
        System.out.println("Press Enter to keep the old value.");
        String name = input.readLine("New name: ");
        String email = input.readLine("New email: ");
        String phone = input.readLine("New phone: ");
        studentService.updateStudent(id, name, email, phone);
        System.out.println("Student updated successfully.");
    }

    private void deleteStudent() throws Exception {
        int id = input.readInt("Student ID to delete: ");
        Student student = studentService.getStudentById(id);
        if (input.confirm("Delete " + student + "? Their enrollments and marks will also be deleted")) {
            studentService.deleteStudent(id);
            System.out.println("Student deleted.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private void printStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.printf("%-6s %-25s %-28s %-15s%n", "ID", "Name", "Email", "Phone");
        System.out.println("-".repeat(76));
        for (Student student : students) {
            student.display();
        }
    }
}
