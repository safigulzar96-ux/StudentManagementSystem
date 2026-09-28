package service;

import dao.EnrollmentDAO;
import exception.DuplicateRecordException;
import exception.RecordNotFoundException;
import model.Enrollment;
import model.RankEntry;
import model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Business rules for enrollment, marks, averages and ranking. */
public class EnrollmentService {

    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public void enroll(int studentId, int courseId)
            throws SQLException, RecordNotFoundException, DuplicateRecordException {
        studentService.getStudentById(studentId);
        courseService.getCourseById(courseId);
        enrollmentDAO.enroll(studentId, courseId);
    }

    public void unenroll(int studentId, int courseId) throws SQLException, RecordNotFoundException {
        if (!enrollmentDAO.unenroll(studentId, courseId)) {
            throw new RecordNotFoundException("That student is not enrolled in that course.");
        }
    }

    public List<Student> getStudentsInCourse(int courseId) throws SQLException, RecordNotFoundException {
        courseService.getCourseById(courseId);
        return studentService.getStudentsByCourse(courseId);
    }

    public List<Enrollment> getEnrollmentsOfStudent(int studentId)
            throws SQLException, RecordNotFoundException {
        studentService.getStudentById(studentId);
        return enrollmentDAO.findByStudent(studentId);
    }

    /** Records new marks or updates old marks (marks must be from 0 to 100). */
    public void recordMarks(int studentId, int courseId, double marks)
            throws SQLException, RecordNotFoundException {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be between 0 and 100.");
        }
        if (!enrollmentDAO.updateMarks(studentId, courseId, marks)) {
            throw new RecordNotFoundException("That student is not enrolled in that course.");
        }
    }

    /** Average of all marked courses, or null if no marks are recorded yet. */
    public Double calculateAverage(List<Enrollment> enrollments) {
        double total = 0;
        int count = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getMarks() != null) {
                total += enrollment.getMarks();
                count++;
            }
        }
        return count == 0 ? null : total / count;
    }

    /** Class ranking: highest average first. Students with equal averages share a rank. */
    public List<RankEntry> getRanking() throws SQLException {
        Map<Integer, Student> students = studentService.getStudentMap();

        // studentId -> list of marks, so we can average them
        Map<Integer, List<Double>> marksByStudent = new HashMap<>();
        for (Enrollment enrollment : enrollmentDAO.findAllWithMarks()) {
            marksByStudent.computeIfAbsent(enrollment.getStudentId(), id -> new ArrayList<>())
                    .add(enrollment.getMarks());
        }

        List<RankEntry> ranking = new ArrayList<>();
        for (Map.Entry<Integer, List<Double>> entry : marksByStudent.entrySet()) {
            Student student = students.get(entry.getKey());
            if (student == null) {
                continue;
            }
            double total = 0;
            for (double mark : entry.getValue()) {
                total += mark;
            }
            double average = total / entry.getValue().size();
            ranking.add(new RankEntry(student.getStudentId(), student.getName(),
                    entry.getValue().size(), average));
        }

        ranking.sort(Comparator.comparingDouble(RankEntry::getAverage).reversed()
                .thenComparing(RankEntry::getStudentName, String.CASE_INSENSITIVE_ORDER));

        int rank = 0;
        Double previousAverage = null;
        for (int i = 0; i < ranking.size(); i++) {
            RankEntry row = ranking.get(i);
            if (previousAverage == null || row.getAverage() != previousAverage) {
                rank = i + 1;
            }
            row.setRank(rank);
            previousAverage = row.getAverage();
        }
        return ranking;
    }
}
