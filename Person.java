package recruitment;

/** Base class for Applicant and Employee (inheritance + polymorphism). */
public abstract class Person {
    private int id;
    private String name;
    private String email;

    public Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$"))
            throw new IllegalArgumentException("Invalid email");
        this.email = email;
    }

    /** Overridden by subclasses (polymorphism). */
    public abstract String getRole();

    public String describe() {
        return getRole() + " #" + id + " | " + name + " | " + email;
    }
}
