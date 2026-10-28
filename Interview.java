package recruitment;

import java.time.LocalDate;

public class Interview {
    public enum Result { PENDING, PASSED, FAILED }

    private static int counter = 1;
    private final int id;
    private final LocalDate date;
    private final String interviewer;
    private Result result = Result.PENDING;

    public Interview(LocalDate date, String interviewer) {
        if (date == null) throw new IllegalArgumentException("Date required");
        if (interviewer == null || interviewer.trim().isEmpty())
            throw new IllegalArgumentException("Interviewer name cannot be empty");
        this.id = counter++;
        this.date = date;
        this.interviewer = interviewer.trim();
    }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getInterviewer() { return interviewer; }
    public Result getResult() { return result; }
    void setResult(Result result) { this.result = result; }

    @Override
    public String toString() {
        return "Interview #" + id + " | " + date + " | Interviewer: " + interviewer + " | Result: " + result;
    }
}
