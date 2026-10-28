package recruitment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Aggregates an Applicant and a JobPosting, and enforces the business rules and status flow. */
public class Application {
    private static int counter = 1;
    private final int id;
    private final Applicant applicant;
    private final JobPosting job;
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    private final List<Interview> interviews = new ArrayList<>();
    private Offer offer;
    private final List<OnboardingTask> tasks = new ArrayList<>();

    public Application(Applicant applicant, JobPosting job) {
        if (applicant == null) throw new IllegalArgumentException("Applicant required");
        if (job == null || !job.isOpen())
            throw new IllegalArgumentException("Applications must reference a valid, open job posting");
        this.id = counter++;
        this.applicant = applicant;
        this.job = job;
    }

    public int getId() { return id; }
    public Applicant getApplicant() { return applicant; }
    public JobPosting getJob() { return job; }
    public ApplicationStatus getStatus() { return status; }
    public Offer getOffer() { return offer; }
    public List<Interview> getInterviews() { return new ArrayList<>(interviews); }
    public List<OnboardingTask> getTasks() { return new ArrayList<>(tasks); }

    private void moveTo(ApplicationStatus next) {
        if (!status.canMoveTo(next))
            throw new IllegalStateException("Invalid status transition: " + status + " -> " + next);
        status = next;
    }

    /** Rule 2: interviews only for active applications. */
    public Interview scheduleInterview(LocalDate date, String interviewer) {
        if (!status.isActive())
            throw new IllegalStateException("Interviews can be scheduled only for active applications (current: " + status + ")");
        Interview interview = new Interview(date, interviewer);
        interviews.add(interview);
        if (status == ApplicationStatus.APPLIED) moveTo(ApplicationStatus.INTERVIEW);
        return interview;
    }

    public void recordInterviewResult(Interview interview, Interview.Result result) {
        interview.setResult(result);
        if (result == Interview.Result.FAILED) moveTo(ApplicationStatus.REJECTED);
    }

    public boolean hasPassedInterview() {
        for (Interview i : interviews)
            if (i.getResult() == Interview.Result.PASSED) return true;
        return false;
    }

    /** Rule 3: offers only after successful interviews. */
    public Offer issueOffer(double salary) {
        if (status != ApplicationStatus.INTERVIEW || !hasPassedInterview())
            throw new IllegalStateException("Offers may be issued only after a successful interview");
        offer = new Offer(salary);
        moveTo(ApplicationStatus.OFFER);
        return offer;
    }

    public void acceptOffer() {
        requireOffer();
        offer.accept();
        moveTo(ApplicationStatus.ONBOARDING);
    }

    public void rejectOffer() {
        requireOffer();
        offer.reject();
        moveTo(ApplicationStatus.REJECTED);
    }

    private void requireOffer() {
        if (offer == null || status != ApplicationStatus.OFFER)
            throw new IllegalStateException("No pending offer for this application");
    }

    /** Rule 4: onboarding tasks created upon offer acceptance. */
    public void createOnboardingTasks() {
        if (status != ApplicationStatus.ONBOARDING || offer.getStatus() != Offer.Status.ACCEPTED)
            throw new IllegalStateException("Tasks can be created only after the offer is accepted");
        if (!tasks.isEmpty())
            throw new IllegalStateException("Onboarding tasks already created");
        tasks.add(new OnboardingTask("Submit identity and education documents"));
        tasks.add(new OnboardingTask("Sign employment contract"));
        tasks.add(new OnboardingTask("Set up email and system access"));
        tasks.add(new OnboardingTask("Attend orientation session"));
    }

    public boolean allTasksDone() {
        if (tasks.isEmpty()) return false;
        for (OnboardingTask t : tasks)
            if (!t.isCompleted()) return false;
        return true;
    }

    public Employee convertToEmployee(LocalDate joinDate) {
        if (status != ApplicationStatus.ONBOARDING)
            throw new IllegalStateException("Only applications in ONBOARDING can become employees");
        if (!allTasksDone())
            throw new IllegalStateException("All onboarding tasks must be completed first");
        moveTo(ApplicationStatus.EMPLOYEE);
        return new Employee(applicant.getName(), applicant.getEmail(), job.getTitle(), offer.getSalary(), joinDate);
    }

    public void printSummary() {
        System.out.println("----------------------------------------");
        System.out.println("Application #" + id + " | " + applicant.getName() + " -> " + job.getTitle());
        System.out.println("Status: " + status);
        if (interviews.isEmpty()) System.out.println("Interviews: none");
        else { System.out.println("Interviews:"); for (Interview i : interviews) System.out.println("  " + i); }
        System.out.println("Offer: " + (offer == null ? "none" : offer.toString()));
        if (!tasks.isEmpty()) {
            System.out.println("Onboarding tasks:");
            int done = 0;
            for (OnboardingTask t : tasks) { System.out.println("  " + t); if (t.isCompleted()) done++; }
            System.out.println("Completion: " + done + "/" + tasks.size());
        }
        System.out.println("----------------------------------------");
    }

    @Override
    public String toString() {
        return "Application #" + id + " | " + applicant.getName() + " | " + job.getTitle() + " | " + status;
    }
}
