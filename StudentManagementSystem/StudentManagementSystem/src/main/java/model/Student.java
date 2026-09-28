package model;

public class Student extends Person implements Displayable {

    private int studentId;

    /** Used when creating a new student (the database gives the ID). */
    public Student(String name, String email, String phone) {
        super(name, email, phone);
    }

    /** Used when reading a student from the database. */
    public Student(int studentId, String name, String email, String phone) {
        super(name, email, phone);
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public void display() {
        System.out.printf("%-6d %-25s %-28s %-15s%n",
                studentId, getName(), getEmail(), getPhone());
    }

    @Override
    public String toString() {
        return getRole() + " #" + studentId + " - " + getName();
    }
}
