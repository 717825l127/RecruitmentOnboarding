package recruitment;

public class Applicant extends Person {
    private static int counter = 1;
    private int experienceYears;

    public Applicant(String name, String email, int experienceYears) {
        super(counter++, name, email);
        setExperienceYears(experienceYears);
    }

    public int getExperienceYears() { return experienceYears; }

    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0 || experienceYears > 50)
            throw new IllegalArgumentException("Experience must be between 0 and 50 years");
        this.experienceYears = experienceYears;
    }

    @Override
    public String getRole() { return "Applicant"; }

    @Override
    public String describe() {
        return super.describe() + " | Experience: " + experienceYears + " yrs";
    }
}
