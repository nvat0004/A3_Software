import java.util.List;

/**
 * The Instructor class extends the User class and represents an instructor
 * in the system. It includes additional attributes like date of birth,
 * gender, qualifications, and specializations. 
 * 
 * This class allows instructors to view their assigned classes, manage
 * their schedules, and includes methods for login, logout, and viewing
 * profiles.
 * 
 * Instructors inherit basic user details from the User class.
 * 
 * @see User
 */
public class Instructor extends User {
    private String dateOfBirth;
    private String gender;
    private String qualifications;
    private String specializations;

    public Instructor(String email, String firstName, String lastName, 
                      String dateOfBirth, String gender, 
                      String qualifications, String specializations) {
        // Provide default values for password and mobileNumber in superclass
        super(email, "defaultPassword", firstName, lastName, 
              "defaultMobileNumber");
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.qualifications = qualifications;
        this.specializations = specializations;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getSpecializations() {
        return specializations;
    }

    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }

    public List<Class> viewAssignedClasses() {
        return null;
    }

    public void manageSchedule() {
    }

    @Override
    public void login() {
    }

    @Override
    public void logout() {
    }

    @Override
    public void viewProfile() {
        System.out.println("Profile: " + getUserDetails() + 
            ", Date of Birth: " + dateOfBirth +
            ", Gender: " + gender + 
            ", Qualifications: " + qualifications +
            ", Specializations: " + specializations);
    }

    @Override
    public void loadFile() {
    }
}
