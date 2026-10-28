package recruitment;

public class JobPosting {
    private static int counter = 1;
    private final int id;
    private String title;
    private String department;
    private boolean open;

    public JobPosting(String title, String department) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be empty");
        if (department == null || department.trim().isEmpty())
            throw new IllegalArgumentException("Department cannot be empty");
        this.id = counter++;
        this.title = title.trim();
        this.department = department.trim();
        this.open = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDepartment() { return department; }
    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }

    @Override
    public String toString() {
        return "Job #" + id + " | " + title + " | " + department + " | " + (open ? "OPEN" : "CLOSED");
    }
}
