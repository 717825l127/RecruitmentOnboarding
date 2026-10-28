package recruitment;

public enum ApplicationStatus {
    APPLIED, INTERVIEW, OFFER, ONBOARDING, EMPLOYEE, REJECTED;

    /** Valid flow: APPLIED -> INTERVIEW -> OFFER -> ONBOARDING -> EMPLOYEE (REJECTED allowed before ONBOARDING). */
    public boolean canMoveTo(ApplicationStatus next) {
        switch (this) {
            case APPLIED:    return next == INTERVIEW || next == REJECTED;
            case INTERVIEW:  return next == OFFER || next == REJECTED;
            case OFFER:      return next == ONBOARDING || next == REJECTED;
            case ONBOARDING: return next == EMPLOYEE;
            default:         return false;
        }
    }

    public boolean isActive() {
        return this == APPLIED || this == INTERVIEW;
    }
}
