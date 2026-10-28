package recruitment;

public class OnboardingTask {
    private static int counter = 1;
    private final int id;
    private final String description;
    private boolean completed;

    public OnboardingTask(String description) {
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty");
        this.id = counter++;
        this.description = description.trim();
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public void markCompleted() { this.completed = true; }

    @Override
    public String toString() {
        return "Task #" + id + " | " + description + " | " + (completed ? "DONE" : "PENDING");
    }
}
