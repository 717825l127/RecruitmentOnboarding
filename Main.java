package recruitment;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final List<JobPosting> jobs = new ArrayList<>();
    private static final List<Applicant> applicants = new ArrayList<>();
    private static final List<Application> applications = new ArrayList<>();
    private static final List<Employee> employees = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Recruitment & Onboarding =====");
            System.out.println("1. Add Job");
            System.out.println("2. Add Applicant");
            System.out.println("3. Apply");
            System.out.println("4. Schedule Interview");
            System.out.println("5. Issue Offer");
            System.out.println("6. Accept or Reject Offer");
            System.out.println("7. Create Onboarding Tasks");
            System.out.println("8. Convert to Employee");
            System.out.println("9. Exit");
            int choice = readInt("Enter choice: ", 1, 9);
            try {
                switch (choice) {
                    case 1: addJob(); break;
                    case 2: addApplicant(); break;
                    case 3: apply(); break;
                    case 4: scheduleInterview(); break;
                    case 5: issueOffer(); break;
                    case 6: respondToOffer(); break;
                    case 7: createTasks(); break;
                    case 8: convertToEmployee(); break;
                    case 9: System.out.println("Goodbye!"); return;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addJob() {
        String title = readText("Job title: ");
        String dept = readText("Department: ");
        JobPosting job = new JobPosting(title, dept);
        jobs.add(job);
        System.out.println("Added: " + job);
    }

    private static void addApplicant() {
        String name = readText("Applicant name: ");
        String email = readEmail("Email: ");
        int exp = readInt("Years of experience (0-50): ", 0, 50);
        Applicant a = new Applicant(name, email, exp);
        applicants.add(a);
        System.out.println("Added: " + a.describe());
    }

    private static void apply() {
        if (jobs.isEmpty() || applicants.isEmpty()) { System.out.println("Add at least one job and one applicant first."); return; }
        for (JobPosting j : jobs) System.out.println(j);
        JobPosting job = findJob(readInt("Job id: ", 1, Integer.MAX_VALUE));
        for (Applicant a : applicants) System.out.println(a.describe());
        Applicant applicant = findApplicant(readInt("Applicant id: ", 1, Integer.MAX_VALUE));
        Application app = new Application(applicant, job);
        applications.add(app);
        System.out.println("Application submitted.");
        app.printSummary();
    }

    private static void scheduleInterview() {
        Application app = pickApplication();
        if (app == null) return;
        LocalDate date = readDate("Interview date (yyyy-MM-dd): ");
        String interviewer = readText("Interviewer name: ");
        Interview interview = app.scheduleInterview(date, interviewer);
        String r = readChoice("Interview result - P (Pass) / F (Fail): ", "PF");
        app.recordInterviewResult(interview, r.equals("P") ? Interview.Result.PASSED : Interview.Result.FAILED);
        app.printSummary();
    }

    private static void issueOffer() {
        Application app = pickApplication();
        if (app == null) return;
        double salary = readDouble("Offered annual salary: ");
        app.issueOffer(salary);
        app.printSummary();
    }

    private static void respondToOffer() {
        Application app = pickApplication();
        if (app == null) return;
        String r = readChoice("Accept or Reject offer - A / R: ", "AR");
        if (r.equals("A")) { app.acceptOffer(); System.out.println("Offer ACCEPTED."); }
        else { app.rejectOffer(); System.out.println("Offer REJECTED."); }
        app.printSummary();
    }

    private static void createTasks() {
        Application app = pickApplication();
        if (app == null) return;
        app.createOnboardingTasks();
        app.printSummary();
    }

    private static void convertToEmployee() {
        Application app = pickApplication();
        if (app == null) return;
        if (app.getStatus() == ApplicationStatus.ONBOARDING) {
            for (OnboardingTask t : app.getTasks()) {
                if (!t.isCompleted()) {
                    String ans = readChoice(t + "\nMark complete? (Y/N): ", "YN");
                    if (ans.equals("Y")) t.markCompleted();
                }
            }
        }
        Employee e = app.convertToEmployee(LocalDate.now());
        employees.add(e);
        System.out.println("Converted to employee: " + e.describe());
        app.printSummary();
    }

    // ---------- lookup helpers ----------
    private static Application pickApplication() {
        if (applications.isEmpty()) { System.out.println("No applications yet."); return null; }
        for (Application a : applications) System.out.println(a);
        int id = readInt("Application id: ", 1, Integer.MAX_VALUE);
        for (Application a : applications) if (a.getId() == id) return a;
        throw new IllegalArgumentException("Application not found");
    }

    private static JobPosting findJob(int id) {
        for (JobPosting j : jobs) if (j.getId() == id) return j;
        throw new IllegalArgumentException("Job posting not found");
    }

    private static Applicant findApplicant(int id) {
        for (Applicant a : applicants) if (a.getId() == id) return a;
        throw new IllegalArgumentException("Applicant not found");
    }

    // ---------- input validation helpers ----------
    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!sc.hasNextLine()) { System.out.println("\nInput ended. Exiting."); System.exit(0); }
        return sc.nextLine().trim();
    }

    private static String readText(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (!s.isEmpty()) return s;
            System.out.println("Input cannot be empty.");
        }
    }

    private static String readEmail(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (s.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) return s;
            System.out.println("Invalid email format.");
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int v = Integer.parseInt(readLine(prompt));
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a number between " + min + " and " + (max == Integer.MAX_VALUE ? "any" : max) + ".");
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                double v = Double.parseDouble(readLine(prompt));
                if (v > 0) return v;
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a positive number.");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            try {
                LocalDate d = LocalDate.parse(readLine(prompt));
                if (!d.isBefore(LocalDate.now())) return d;
                System.out.println("Date cannot be in the past.");
            } catch (DateTimeParseException e) {
                System.out.println("Use format yyyy-MM-dd.");
            }
        }
    }

    private static String readChoice(String prompt, String allowed) {
        while (true) {
            String s = readLine(prompt).toUpperCase();
            if (s.length() == 1 && allowed.contains(s)) return s;
            System.out.println("Enter one of: " + allowed.replace("", " ").trim());
        }
    }
}
