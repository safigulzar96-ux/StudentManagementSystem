package model;

/** One row of the class ranking report. */
public class RankEntry {

    private final int studentId;
    private final String studentName;
    private final int coursesMarked;
    private final double average;
    private int rank;

    public RankEntry(int studentId, String studentName, int coursesMarked, double average) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.coursesMarked = coursesMarked;
        this.average = average;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getCoursesMarked() {
        return coursesMarked;
    }

    public double getAverage() {
        return average;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
