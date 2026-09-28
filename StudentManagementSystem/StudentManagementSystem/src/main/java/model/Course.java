package model;

public class Course implements Displayable {

    private int courseId;
    private String courseCode;
    private String courseName;
    private int creditHours;

    public Course(String courseCode, String courseName, int creditHours) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
    }

    public Course(int courseId, String courseCode, String courseName, int creditHours) {
        this(courseCode, courseName, creditHours);
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    @Override
    public void display() {
        System.out.printf("%-6d %-12s %-30s %-6d%n",
                courseId, courseCode, courseName, creditHours);
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }
}
