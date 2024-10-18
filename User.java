/**
 * The abstract User class serves as a base class for different types of
 * users in the system. It includes common attributes like email, password,
 * first name, last name, and mobile number.
 * 
 * This class provides both abstract methods, which must be implemented by
 * subclasses, and concrete methods to manage user attributes and update
 * user details.
 * 
 * @see Member
 * @see Instructor
 * @see Administrator
 * @author Neeraj Vats
 */
public abstract class User {
    // Attributes
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String mobileNumber;

    /**
     * Constructor to initialize a User object.
     * 
     * @param email        The user's email address
     * @param password     The user's password
     * @param firstName    The user's first name
     * @param lastName     The user's last name
     * @param mobileNumber The user's mobile number
     */
    public User(String email, String password, String firstName, 
                String lastName, String mobileNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
    }

    // Abstract methods that subclasses must implement

    /**
     * Abstract method for user login, to be implemented by subclasses.
     */
    public abstract void login();

    /**
     * Abstract method for user logout, to be implemented by subclasses.
     */
    public abstract void logout();

    /**
     * Abstract method to view the user's profile, to be implemented by
     * subclasses.
     */
    public abstract void viewProfile();

    /**
     * Abstract method to load file-related information, to be implemented by
     * subclasses.
     */
    public abstract void loadFile();

    // Concrete methods for setting and getting user attributes

    /**
     * Gets the user's email address.
     * 
     * @return The user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's password.
     * 
     * @return The user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * 
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's first name.
     * 
     * @return The user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     * 
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     * 
     * @return The user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     * 
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's mobile number.
     * 
     * @return The user's mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the user's mobile number.
     * 
     * @param mobileNumber The mobile number to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // Additional methods for managing user details

    /**
     * Gets the full user details including name, mobile number, and email.
     * 
     * @return A string with the user's full details
     */
    public String getUserDetails() {
        return "Name: " + firstName + " " + lastName + 
               ", Mobile: " + mobileNumber + 
               ", Email: " + email;
    }

    /**
     * Updates multiple user details at once, including name, mobile number,
     * and email.
     * 
     * @param firstName    The user's first name to update
     * @param lastName     The user's last name to update
     * @param mobileNumber The user's mobile number to update
     * @param email        The user's email to update
     */
    public void setUserDetails(String firstName, String lastName, 
                               String mobileNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    /**
     * Allows the user to edit their profile details including first name,
     * last name, and mobile number.
     * 
     * @param firstName    The first name to set
     * @param lastName     The last name to set
     * @param mobileNumber The mobile number to set
     */
    public void editProfile(String firstName, String lastName, 
                            String mobileNumber) {
        setFirstName(firstName);
        setLastName(lastName);
        setMobileNumber(mobileNumber);
    }
}
