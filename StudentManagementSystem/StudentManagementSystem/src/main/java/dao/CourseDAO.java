package dao;

import exception.DuplicateRecordException;
import model.Course;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** All SQL for the courses table. */
public class CourseDAO {

    public int addCourse(Course course) throws SQLException, DuplicateRecordException {
        String sql = "INSERT INTO courses (course_code, course_name, credit_hours) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, course.getCourseCode());
            ps.setString(2, course.getCourseName());
            ps.setInt(3, course.getCreditHours());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateRecordException("A course with code " + course.getCourseCode() + " already exists.");
        }
    }

    public Course findById(int courseId) throws SQLException {
        String sql = "SELECT course_id, course_code, course_name, credit_hours FROM courses WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<Course> findAll() throws SQLException {
        String sql = "SELECT course_id, course_code, course_name, credit_hours FROM courses";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                courses.add(mapRow(rs));
            }
        }
        return courses;
    }

    /** Returns true if a row was deleted. Enrollments are removed by the database (ON DELETE CASCADE). */
    public boolean deleteCourse(int courseId) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            return ps.executeUpdate() > 0;
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        return new Course(rs.getInt("course_id"), rs.getString("course_code"),
                rs.getString("course_name"), rs.getInt("credit_hours"));
    }
}
