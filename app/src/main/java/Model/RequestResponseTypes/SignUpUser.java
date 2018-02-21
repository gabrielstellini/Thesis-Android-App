package Model.RequestResponseTypes;

import java.util.Date;

public class SignUpUser {
    private String username;
    private boolean hasMedication;
    private boolean hasFinancialPressure;
    private boolean hasEmotionalSupport;
    private boolean isMale;
    private Date dateOfBirth;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean hasMedication() {
        return hasMedication;
    }

    public void setHasMedication(boolean hasMedication) {
        this.hasMedication = hasMedication;
    }

    public boolean hasFinancialPressure() {
        return hasFinancialPressure;
    }

    public void setHasFinancialPressure(boolean hasFinancialPressure) {
        this.hasFinancialPressure = hasFinancialPressure;
    }

    public boolean hasEmotionalSupport() {
        return hasEmotionalSupport;
    }

    public void setHasEmotionalSupport(boolean hasEmotionalSupport) {
        this.hasEmotionalSupport = hasEmotionalSupport;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
