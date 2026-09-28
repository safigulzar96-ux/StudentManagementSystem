package model;

/** Links one student to one course and stores the marks (marks is null until recorded). */
public class Enrollment {

    private int enrollmentId;
    private int studentId;
    private int courseId;
    private String courseCode;
    private String courseName;
    private Double marks;

    public Enrollment(int enrollmentId, int studentId, int courseId,
                      String courseCode, String courseName, Double marks) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.marks = marks;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }
}
