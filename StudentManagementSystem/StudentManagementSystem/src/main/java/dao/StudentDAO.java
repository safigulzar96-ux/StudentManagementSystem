package dao;

import exception.DuplicateRecordException;
import model.Student;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** All SQL for the students table. */
public class StudentDAO {

    public int addStudent(Student student) throws SQLException, DuplicateRecordException {
        String sql = "INSERT INTO students (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateRecordException("A student with email " + student.getEmail() + " already exists.");
        }
    }

    public Student findById(int studentId) throws SQLException {
        String sql = "SELECT student_id, name, email, phone FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT student_id, name, email, phone FROM students";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                students.add(mapRow(rs));
            }
        }
        return students;
    }

    public List<Student> searchByName(String keyword) throws SQLException {
        String sql = "SELECT student_id, name, email, phone FROM students WHERE name LIKE ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }
        }
        return students;
    }

    public List<Student> findByCourse(int courseId) throws SQLException {
        String sql = "SELECT s.student_id, s.name, s.email, s.phone "
                + "FROM students s JOIN enrollments e ON s.student_id = e.student_id "
                + "WHERE e.course_id = ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }
        }
        return students;
    }

    /** Returns true if a row was updated. */
    public boolean updateStudent(Student student) throws SQLException, DuplicateRecordException {
        String sql = "UPDATE students SET name = ?, email = ?, phone = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setInt(4, student.getStudentId());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateRecordException("Another student already uses email " + student.getEmail() + ".");
        }
    }

    /** Returns true if a row was deleted. Enrollments are removed by the database (ON DELETE CASCADE). */
    public boolean deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("student_id"), rs.getString("name"),
                rs.getString("email"), rs.getString("phone"));
    }
}
