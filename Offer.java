package recruitment;

public class Offer {
    public enum Status { ISSUED, ACCEPTED, REJECTED }

    private static int counter = 1;
    private final int id;
    private final double salary;
    private Status status = Status.ISSUED;

    public Offer(double salary) {
        if (salary <= 0) throw new IllegalArgumentException("Salary must be positive");
        this.id = counter++;
        this.salary = salary;
    }

    public int getId() { return id; }
    public double getSalary() { return salary; }
    public Status getStatus() { return status; }

    void accept() {
        if (status != Status.ISSUED) throw new IllegalStateException("Offer already " + status);
        status = Status.ACCEPTED;
    }

    void reject() {
        if (status != Status.ISSUED) throw new IllegalStateException("Offer already " + status);
        status = Status.REJECTED;
    }

    @Override
    public String toString() {
        return "Offer #" + id + " | Salary: " + salary + " | Status: " + status;
    }
}
