package recruitment;

import java.time.LocalDate;

public class Employee extends Person {
    private static int counter = 1000;
    private String jobTitle;
    private double salary;
    private LocalDate joinDate;

    public Employee(String name, String email, String jobTitle, double salary, LocalDate joinDate) {
        super(counter++, name, email);
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.joinDate = joinDate;
    }

    public String getJobTitle() { return jobTitle; }
    public double getSalary() { return salary; }
    public LocalDate getJoinDate() { return joinDate; }

    @Override
    public String getRole() { return "Employee"; }

    @Override
    public String describe() {
        return super.describe() + " | " + jobTitle + " | Salary: " + salary + " | Joined: " + joinDate;
    }
}
