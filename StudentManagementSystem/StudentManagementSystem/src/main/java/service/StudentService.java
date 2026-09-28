package service;

import dao.StudentDAO;
import exception.DuplicateRecordException;
import exception.RecordNotFoundException;
import model.Student;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Business rules for students. */
public class StudentService {

    private final StudentDAO studentDAO = new StudentDAO();

    public int addStudent(String name, String email, String phone)
            throws SQLException, DuplicateRecordException {
        validate(name, email);
        return studentDAO.addStudent(new Student(name, email, phone));
    }

    public Student getStudentById(int studentId) throws SQLException, RecordNotFoundException {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new RecordNotFoundException("No student found with ID " + studentId + ".");
        }
        return student;
    }

    /** All students, sorted by name (A to Z). */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = studentDAO.findAll();
        students.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        return students;
    }

    public List<Student> searchByName(String keyword) throws SQLException {
        List<Student> students = studentDAO.searchByName(keyword);
        students.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        return students;
    }

    public List<Student> getStudentsByCourse(int courseId) throws SQLException {
        List<Student> students = studentDAO.findByCourse(courseId);
        students.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        return students;
    }

    /** Fast lookup: student ID -> Student. */
    public Map<Integer, Student> getStudentMap() throws SQLException {
        Map<Integer, Student> map = new HashMap<>();
        for (Student student : studentDAO.findAll()) {
            map.put(student.getStudentId(), student);
        }
        return map;
    }

    /** Empty new values mean "keep the old value". */
    public void updateStudent(int studentId, String newName, String newEmail, String newPhone)
            throws SQLException, RecordNotFoundException, DuplicateRecordException {
        Student student = getStudentById(studentId);
        if (!newName.isEmpty()) {
            student.setName(newName);
        }
        if (!newEmail.isEmpty()) {
            student.setEmail(newEmail);
        }
        if (!newPhone.isEmpty()) {
            student.setPhone(newPhone);
        }
        validate(student.getName(), student.getEmail());
        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int studentId) throws SQLException, RecordNotFoundException {
        if (!studentDAO.deleteStudent(studentId)) {
            throw new RecordNotFoundException("No student found with ID " + studentId + ".");
        }
    }

    private void validate(String name, String email) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (email == null || !email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
    }
}
