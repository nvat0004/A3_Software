import java.util.ArrayList;
import java.util.List;

/**
 * The Member class extends the User class and represents a member in the
 * system. It includes additional attributes such as date of birth, gender,
 * address, credit balance, previous classes, and transaction history.
 * 
 * This class provides methods for managing credit balance, booking and
 * canceling classes, viewing transaction history, and overriding login and
 * profile methods from the User class.
 * 
 * @see User
 * @see BookedClass
 * @see Transaction
 * @author Aryan
 */
public class Member extends User {
    private String dateOfBirth;
    private String gender;
    private String address;
    private List<BookedClass> previousClasses;
    private List<Transaction> transactionHistory;
    private double creditBalance = 1000; // Default balance for new members

    /**
     * Constructor to initialize a Member object.
     * 
     * @param email            The member's email
     * @param password         The member's password
     * @param firstName        The member's first name
     * @param lastName         The member's last name
     * @param mobileNumber     The member's mobile number
     * @param dateOfBirth      The member's date of birth (dd/mm/yyyy)
     * @param gender           The member's gender
     * @param address          The member's address
     * @param creditBalance    The member's initial credit balance
     * @param previousClasses  List of the member's previously booked classes
     */
    public Member(String email, String password, String firstName, 
                  String lastName, String mobileNumber, String dateOfBirth, 
                  String gender, String address, double creditBalance, 
                  List<BookedClass> previousClasses) {
        super(email, password, firstName, lastName, mobileNumber);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.creditBalance = creditBalance;
        this.previousClasses = previousClasses;
        this.transactionHistory = new ArrayList<>(); // Initialize history
    }

    // Methods for managing credit balance

    /**
     * Views the member's current credit balance.
     */
    public void viewCreditBalance() {
        System.out.println("Your current credit balance: " + getCreditBalance());
    }

    /**
     * Deducts credits from the member's balance when booking a class.
     * 
     * @param amount The amount of credits to deduct
     */
    public void deductCredits(int amount) {
        if (this.creditBalance >= amount) {
            this.creditBalance -= amount;
        } else {
            System.out.println("Insufficient credits to complete the booking.");
        }
    }

    /**
     * Adds credits to the member's balance (e.g., after a refund or by admin).
     * 
     * @param amount The amount of credits to add
     */
    public void addCredits(int amount) {
        this.creditBalance += amount;
    }

    // Getters and setters for member-specific attributes

    /**
     * Gets the member's date of birth.
     * 
     * @return The member's date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the member's date of birth.
     * 
     * @param dateOfBirth The member's date of birth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the member's gender.
     * 
     * @return The member's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the member's gender.
     * 
     * @param gender The gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the member's address.
     * 
     * @return The member's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the member's address.
     * 
     * @param address The address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the member's credit balance.
     * 
     * @return The member's current credit balance
     */
    public double getCreditBalance() {
        return creditBalance;
    }

    /**
     * Sets the member's credit balance.
     * 
     * @param creditBalance The credit balance to set
     */
    public void setCreditBalance(double creditBalance) {
        this.creditBalance = creditBalance;
    }

    /**
     * Gets the member's transaction history.
     * 
     * @return The member's transaction history
     */
    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Sets the member's transaction history.
     * 
     * @param transactionHistory The transaction history to set
     */
    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    // Methods for class booking and transaction management

    /**
     * Displays the list of available booked classes for the member.
     * 
     * @param classes List of classes available for booking
     */
    public void viewAvailableBookedClasses(List<BookedClass> classes) {
        for (BookedClass c : classes) {
            System.out.println("BookedClass: " + c.getClassName() + 
                ", Instructor: " + c.getInstructor().getFirstName() + 
                ", Date: " + c.getDate() + ", Time: " + c.getTime() + 
                ", Location: " + c.getLocation() + 
                ", Credit Points: " + c.getCreditPoints() + 
                ", Capacity: " + c.getCapacity());
        }
    }

    /**
     * Displays the member's previously booked classes.
     */
    public void viewPreviousClasses() {
        for (BookedClass c : previousClasses) {
            System.out.println("Class Name: " + c.getClassName() + 
                ", Date: " + c.getDate() + ", Time: " + c.getTime() + 
                ", Location: " + c.getLocation() + 
                ", Instructor: " + c.getInstructor().getFirstName() + 
                ", Attendance Status: " + c.getAttendanceStatus());
        }
    }

    /**
     * Books a class for the member, deducting credits and reducing class
     * capacity.
     * 
     * @param c The class to be booked
     */
    public void bookBookedClass(BookedClass c) {
        if (c.checkAvailability()) {
            if (this.creditBalance >= c.getCreditPoints()) {
                // Deduct credits and reduce capacity
                this.creditBalance -= c.getCreditPoints();
                c.setCapacity(c.getCapacity() - 1);

                // Record transaction
                transactionHistory.add(new Transaction(
                    "TXN" + System.currentTimeMillis(), new java.util.Date(), 
                    "BOOKING", c.getCreditPoints(), this));

                System.out.println("Booking successful for class: " + 
                                   c.getClassName());
                System.out.println("Remaining Credit Balance: " + 
                                   this.creditBalance);
            } else {
                System.out.println("Insufficient credits to book this class.");
            }
        } else {
            System.out.println("No available slots for this class.");
        }
    }

    /**
     * Cancels a booking for the member and processes refunds based on the
     * time of cancellation.
     * 
     * @param c                  The class to be canceled
     * @param isWithinAllowedTime True if canceled within allowed time frame
     */
    public void cancelBooking(BookedClass c, boolean isWithinAllowedTime) {
        if (isWithinAllowedTime) {
            // Full refund
            this.creditBalance += c.getCreditPoints();
            c.setCapacity(c.getCapacity() + 1);
            System.out.println("Class cancelled successfully, full credits refunded.");

            // Record transaction
            transactionHistory.add(new Transaction(
                "TXN" + System.currentTimeMillis(), new java.util.Date(), 
                "REFUND", c.getCreditPoints(), this));
        } else {
            // Apply 50% penalty
            int refundAmount = c.getCreditPoints() / 2;
            this.creditBalance += refundAmount;
            c.setCapacity(c.getCapacity() + 1);
            System.out.println("Class cancelled with 50% refund. Refunded: " + 
                               refundAmount + " credits.");

            // Record transaction
            transactionHistory.add(new Transaction(
                "TXN" + System.currentTimeMillis(), new java.util.Date(), 
                "REFUND", refundAmount, this));
        }

        System.out.println("Remaining Credit Balance: " + this.creditBalance);
    }

    /**
     * Displays the member's transaction history.
     */
    public void viewTransactionHistory() {
        System.out.println("Transaction History:");
        for (Transaction t : transactionHistory) {
            System.out.println("Transaction ID: " + t.getTransactionId() + 
                               ", Date: " + t.getDate() + 
                               ", Type: " + t.getType() + 
                               ", Amount: " + t.getAmount());
        }
    }

    // Override methods for login, logout, and profile viewing

    /**
     * Logs in the member with predefined credentials for testing purposes.
     */
    @Override
    public void login() {
        String correctEmail = "member@student.monash.edu";
        String correctPassword = "Monash1234!";

        if (this.getEmail().equals(correctEmail) && 
            this.getPassword().equals(correctPassword)) {
            System.out.println("Member login successful.");
        } else {
            System.out.println("Invalid credentials for Member.");
        }
    }

    /**
     * Logs out the member.
     */
    @Override
    public void logout() {
        System.out.println("Member logged out.");
    }

    /**
     * Displays the member's profile, including personal details.
     */
    @Override
    public void viewProfile() {
        System.out.println("Profile: " + getUserDetails() + 
            ", Date of Birth: " + dateOfBirth + 
            ", Gender: " + gender + 
            ", Address: " + address);
    }

    public List<BookedClass> getPreviousClasses() {
        return previousClasses;
    }
    

    /**
     * Method to load file-related information for the member (if needed).
     */
    @Override
    public void loadFile() {
        // Implement file loading logic here if needed
    }
}
