package model;

/** Abstract parent class for people stored in the system. */
public abstract class Person {

    private String name;
    private String email;
    private String phone;

    protected Person(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** Each type of person tells us its own role (polymorphism). */
    public abstract String getRole();
}
