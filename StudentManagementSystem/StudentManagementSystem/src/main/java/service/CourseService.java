package service;

import dao.CourseDAO;
import exception.DuplicateRecordException;
import exception.RecordNotFoundException;
import model.Course;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

/** Business rules for courses. */
public class CourseService {

    private final CourseDAO courseDAO = new CourseDAO();

    public int addCourse(String code, String name, int creditHours)
            throws SQLException, DuplicateRecordException {
        if (code.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException("Course code and name cannot be empty.");
        }
        if (creditHours < 1 || creditHours > 10) {
            throw new IllegalArgumentException("Credit hours must be between 1 and 10.");
        }
        return courseDAO.addCourse(new Course(code.toUpperCase(), name, creditHours));
    }

    public Course getCourseById(int courseId) throws SQLException, RecordNotFoundException {
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new RecordNotFoundException("No course found with ID " + courseId + ".");
        }
        return course;
    }

    /** All courses, sorted by course code. */
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = courseDAO.findAll();
        courses.sort(Comparator.comparing(Course::getCourseCode));
        return courses;
    }

    public void deleteCourse(int courseId) throws SQLException, RecordNotFoundException {
        if (!courseDAO.deleteCourse(courseId)) {
            throw new RecordNotFoundException("No course found with ID " + courseId + ".");
        }
    }
}
