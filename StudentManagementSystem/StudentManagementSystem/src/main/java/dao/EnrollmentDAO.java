package dao;

import exception.DuplicateRecordException;
import model.Enrollment;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/** All SQL for the enrollments table (which also stores the marks). */
public class EnrollmentDAO {

    public void enroll(int studentId, int courseId) throws SQLException, DuplicateRecordException {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateRecordException("This student is already enrolled in that course.");
        }
    }

    /** Returns true if an enrollment was removed. */
    public boolean unenroll(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Returns true if the student is enrolled in the course and the marks were saved. */
    public boolean updateMarks(int studentId, int courseId, double marks) throws SQLException {
        String sql = "UPDATE enrollments SET marks = ? WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, marks);
            ps.setInt(2, studentId);
            ps.setInt(3, courseId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Enrollment> findByStudent(int studentId) throws SQLException {
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.marks, c.course_code, c.course_name "
                + "FROM enrollments e JOIN courses c ON e.course_id = c.course_id "
                + "WHERE e.student_id = ? ORDER BY c.course_code";
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapRow(rs));
                }
            }
        }
        return enrollments;
    }

    /** All enrollments that already have marks (used for the ranking report). */
    public List<Enrollment> findAllWithMarks() throws SQLException {
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.marks, c.course_code, c.course_name "
                + "FROM enrollments e JOIN courses c ON e.course_id = c.course_id "
                + "WHERE e.marks IS NOT NULL";
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                enrollments.add(mapRow(rs));
            }
        }
        return enrollments;
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        double marksValue = rs.getDouble("marks");
        Double marks = rs.wasNull() ? null : marksValue;
        return new Enrollment(rs.getInt("enrollment_id"), rs.getInt("student_id"),
                rs.getInt("course_id"), rs.getString("course_code"),
                rs.getString("course_name"), marks);
    }
}
