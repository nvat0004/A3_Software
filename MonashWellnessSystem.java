import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MonashWellnessSystem {
    private static final String USERS_FILE = "users.txt";
    private static final String CLASSES_FILE = "classes.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";
    private static final String CREDIT_HISTORY_FILE = "credit_history.txt";

    private static boolean isLoggedIn = false;
    private static String loggedInUser = "";
    private static Member member = null;
    private static Administrator admin = null;

    public static boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(String user) {
        loggedInUser = user;
    }

    public static Member getMember() {
        return member;
    }

    public static void setMember(Member memberObj) {
        member = memberObj;
    }

    public static Administrator getAdmin() {
        return admin;
    }

    public static void setAdmin(Administrator adminObj) {
        admin = adminObj;
    }

    // Login Method
    public static void login(Scanner scanner) {
        if (isLoggedIn) {
            System.out.println("Already logged in as " + loggedInUser);
            return;
        }

        String allowedEmail1 = "member@student.monash.edu";
        String allowedPassword1 = "Monash1234!";
        
        String allowedEmail2 = "admin@monash.edu";
        String allowedPassword2 = "Admin1234!";

        System.out.print("Enter Email: ");
        String email = scanner.next();
        
        if (!email.equals(allowedEmail1) && !email.equals(allowedEmail2)) {
            System.out.println("Wrong email ID. Please try again.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.next();

        if (email.equals(allowedEmail1) && password.equals(allowedPassword1)) {
            isLoggedIn = true;
            loggedInUser = "Member";

            // Initialize the member object with sample data
            member = new Member(
                email,
                password,
                "John",  // First Name
                "Doe",   // Last Name
                "0400000000",  // Mobile Number
                "01/01/2000",  // Date of Birth
                "Male",        // Gender
                "123 Street, Melbourne",  // Address
                1000,  // Initial Credit Balance
                new ArrayList<>()  // Empty Previous Classes List
            );

            System.out.println("Member login successful.");
        } else if (email.equals(allowedEmail2) && password.equals(allowedPassword2)) {
            isLoggedIn = true;
            loggedInUser = "Admin";
            System.out.println("Admin login successful.");
        } else {
            System.out.println("Invalid password. Please try again.");
        }
    }

    // Logout function to reset login state
    public static void logout() {
        if (isLoggedIn) {
            System.out.println(loggedInUser + " logged out.");
            isLoggedIn = false;
            loggedInUser = "";
            member = null;
            admin = null;
        } else {
            System.out.println("No user is logged in.");
        }
    }

    // Book Class Feature
    public static void bookClass(Scanner scanner) {
        viewClassListings();

        if (member == null) {
            System.out.println("You need to be logged in as a member to book a class.");
            return;
        }

        System.out.print("Enter the class name to book: ");
        String className = scanner.next();
        
        System.out.print("Enter the date (dd/MM/yyyy): ");
        String date = scanner.next();
        
        System.out.print("Enter the time (HH:mm): ");
        String time = scanner.next();

        // Check if the booking is being made 2 hours ahead of the class
        if (!isBookingAllowed(date, time)) {
            System.out.println("Booking must be made at least 2 hours ahead of the class.");
            return;
        }

        // Check for scheduling conflicts
        if (hasSchedulingConflict(date, time)) {
            System.out.println("You have a scheduling conflict with another class. Please choose another time.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CLASSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] classData = line.split(",");
                if (classData[0].equalsIgnoreCase(className)) {
                    int capacity = Integer.parseInt(classData[8]);
                    int creditPoints = Integer.parseInt(classData[6]);
                    int duration = Integer.parseInt(classData[4]); // Duration of class

                    if (capacity > 0 && member.getCreditBalance() >= creditPoints) {
                        // Decrease capacity
                        capacity--;
                        // Deduct credits
                        member.setCreditBalance(member.getCreditBalance() - creditPoints);

                        // Create the Instructor object
                        Instructor instructor = new Instructor(
                            classData[1], "DefaultLastName", classData[2], "01/01/1980", 
                            "Male", "Qualified", "Specialization"
                        );

                        // Create the BookedClass object
                        BookedClass bookedClass = new BookedClass(
                            className, 
                            instructor, 
                            date, 
                            time, 
                            duration, 
                            classData[7],  // Location
                            capacity, 
                            classData[9],  // Category
                            classData[10], // Difficulty Level
                            creditPoints
                        );

                        // Add the booked class to the member's previousClasses list
                        member.getPreviousClasses().add(bookedClass);

                        // Save booking to file
                        saveBooking(className, date, time, classData[7], creditPoints);
                        
                        // Display booking confirmation
                        System.out.println("Class booked successfully!");
                        System.out.println("Class Details:");
                        System.out.println("Class Name: " + className);
                        System.out.println("Date: " + date);
                        System.out.println("Time: " + time);
                        System.out.println("Location: " + classData[7]);
                        System.out.println("Instructor: " + classData[1]);
                        System.out.println("Remaining Credits: " + member.getCreditBalance());

                    } else {
                        System.out.println("Insufficient capacity or credits.");
                    }
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading class file.");
        }
    }

    // Cancel a Booking with Cutoff and Penalty
    public static void cancelBooking(Scanner scanner) {
        if (member == null || member.getPreviousClasses().isEmpty()) {
            System.out.println("You have no bookings to cancel.");
            return;
        }

        System.out.println("Your Booked Classes:");
        List<BookedClass> previousClasses = member.getPreviousClasses();
        for (int i = 0; i < previousClasses.size(); i++) {
            BookedClass bookedClass = previousClasses.get(i);
            System.out.println((i + 1) + ". " + bookedClass.getClassName() + " on " + bookedClass.getDate());
        }

        System.out.print("Enter the number of the class to cancel: ");
        
        // Ensure valid number input
        try {
            int classIndex = scanner.nextInt() - 1;

            if (classIndex >= 0 && classIndex < previousClasses.size()) {
                BookedClass bookedClass = previousClasses.get(classIndex);

                // Calculate if the cancellation is within 24 hours
                if (isWithin24Hours(bookedClass.getDate(), bookedClass.getTime())) {
                    int penalty = bookedClass.getCreditPoints() / 2; // 50% penalty
                    member.setCreditBalance(member.getCreditBalance() + penalty);
                    System.out.println("Class canceled within 24 hours. 50% credits refunded: " + penalty + ". Remaining balance: " + member.getCreditBalance());
                } else {
                    member.setCreditBalance(member.getCreditBalance() + bookedClass.getCreditPoints());
                    System.out.println("Class canceled successfully. Full credits refunded: " + bookedClass.getCreditPoints() + ". Remaining balance: " + member.getCreditBalance());
                }

                // Update capacity for the canceled class
                updateClassCapacity(bookedClass.getClassName(), bookedClass.getCapacity() + 1);

                // Remove the booking from the member's list
                previousClasses.remove(classIndex);

            } else {
                System.out.println("Invalid class selection.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Clear the invalid input
        }
    }

    // Helper Method to Check if Cancellation is Within 24 Hours
    public static boolean isWithin24Hours(String classDate, String classTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date currentDateTime = new Date();  // Current date and time
            Date classDateTime = dateFormat.parse(classDate + " " + classTime);

            // Calculate the difference in milliseconds
            long difference = classDateTime.getTime() - currentDateTime.getTime();

            // Check if the cancellation is within 24 hours (86400000 ms = 24 hours)
            return difference <= 86400000;
        } catch (Exception e) {
            System.out.println("Error in date/time parsing.");
            return false;
        }
    }

    // View Previous Classes (Attended, Canceled)
    public static void viewPreviousClasses() {
        if (member == null || member.getPreviousClasses().isEmpty()) {
            System.out.println("You have no previous classes.");
            return;
        }

        System.out.println("Your Previous Classes:");
        List<BookedClass> previousClasses = member.getPreviousClasses();
        for (BookedClass bookedClass : previousClasses) {
            System.out.println("Class Name: " + bookedClass.getClassName() + 
                               ", Date: " + bookedClass.getDate() + 
                               ", Time: " + bookedClass.getTime() + 
                               ", Location: " + bookedClass.getLocation() + 
                               ", Instructor: " + bookedClass.getInstructor().getFirstName() +
                               ", Attendance Status: " + bookedClass.getAttendanceStatus());
        }
    }

    // Provide Feedback for a Class
    public static void provideFeedback(Scanner scanner) {
        if (member == null || member.getPreviousClasses().isEmpty()) {
            System.out.println("You have no classes to provide feedback for.");
            return;
        }

        System.out.println("Your Completed Classes:");
        List<BookedClass> previousClasses = member.getPreviousClasses();
        for (int i = 0; i < previousClasses.size(); i++) {
            BookedClass bookedClass = previousClasses.get(i);
            System.out.println((i + 1) + ". " + bookedClass.getClassName() + " on " + bookedClass.getDate());
        }

        System.out.print("Enter the number of the class to provide feedback: ");
        int classIndex = scanner.nextInt() - 1;

        if (classIndex >= 0 && classIndex < previousClasses.size()) {
            BookedClass bookedClass = previousClasses.get(classIndex);

            System.out.print("Enter feedback for the class: ");
            String feedback = scanner.next();
            
            // Prompt for rating
            System.out.print("Enter rating (1-5): ");
            int rating = scanner.nextInt();

            // Prompt for anonymous feedback
            System.out.print("Is the feedback anonymous? (true/false): ");
            boolean isAnonymous = scanner.nextBoolean();

            bookedClass.addFeedback(new Feedback(feedback, rating, isAnonymous));

            System.out.println("Thank you for your feedback on " + bookedClass.getClassName() + "!");
        } else {
            System.out.println("Invalid class selection.");
        }
    }

    // View Class Listings
    public static void viewClassListings() {
        System.out.println("Available Classes:");
        try (BufferedReader br = new BufferedReader(new FileReader(CLASSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] classData = line.split(",");
                System.out.println("Class: " + classData[0] +
                        ", Instructor: " + classData[1] +
                        ", Date: " + classData[2] +
                        ", Time: " + classData[3] +
                        ", Duration: " + classData[4] +
                        ", Occurrence: " + classData[5] +
                        ", Credit Points: " + classData[6] +
                        ", Location: " + classData[7] +
                        ", Capacity: " + classData[8] +
                        ", Category: " + classData[9] +
                        ", Difficulty Level: " + classData[10]);
            }
        } catch (IOException e) {
            System.out.println("Error reading class file.");
        }
    }

    // Save Booking
    private static void saveBooking(String className, String date, String time,
                                    String location, int creditPoints) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE, true))) {
            bw.write(loggedInUser + "," + className + "," + date + "," + time + "," +
                     location + "," + creditPoints + "\n");
        } catch (IOException e) {
            System.out.println("Error saving booking.");
        }
    }

    // Check for Scheduling Conflict
    public static boolean hasSchedulingConflict(String date, String time) {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] bookingData = line.split(",");
                if (bookingData[0].equals(loggedInUser) && bookingData[2].equals(date) && bookingData[3].equals(time)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading bookings file.");
        }
        return false;
    }

    // Check if Booking is Allowed (at least 2 hours ahead)
    public static boolean isBookingAllowed(String date, String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date currentDateTime = new Date();  // Current date and time
            Date classDateTime = dateFormat.parse(date + " " + time);

            // Calculate the difference in milliseconds
            long difference = classDateTime.getTime() - currentDateTime.getTime();

            // Check if the booking is being made at least 2 hours in advance (7200000 ms = 2 hours)
            return difference >= 7200000;
        } catch (Exception e) {
            System.out.println("Error in date/time parsing.");
            return false;
        }
    }

    // Update Class Capacity after Cancellation
    private static void updateClassCapacity(String className, int newCapacity) {
        try (BufferedReader br = new BufferedReader(new FileReader(CLASSES_FILE))) {
            List<String> updatedClasses = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] classData = line.split(",");
                if (classData[0].equalsIgnoreCase(className)) {
                    classData[8] = String.valueOf(newCapacity); // Update the capacity field
                }
                updatedClasses.add(String.join(",", classData));
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CLASSES_FILE, false))) {
                for (String updatedClass : updatedClasses) {
                    bw.write(updatedClass + "\n");
                }
            }

        } catch (IOException e) {
            System.out.println("Error updating class capacity.");
        }
    }

    
    // Studio Capacity Feature
    private static int getStudioCapacity(int studio) {
        if (studio >= 1 && studio <= 3) return 25;
        if (studio >= 4 && studio <= 5) return 40;
        if (studio == 6) return 50;
        return 0;  // Invalid studio
    }

    // Admin: Add Credits to Member (Now logs credit history)
public static void addCreditsToMember(Scanner scanner) {
    if (!loggedInUser.equals("Admin")) {
        System.out.println("Only admins can add credits.");
        return;
    }

    System.out.print("Enter member's email: ");
    String memberEmail = scanner.next();

    Member member = findMemberByEmail(memberEmail);
    if (member == null) {
        System.out.println("Member not found.");
        return;
    }

    System.out.print("Enter credits to add (max 1000): ");
    int creditsToAdd = scanner.nextInt();

    if (creditsToAdd > 1000) {
        System.out.println("You cannot add more than 1000 credits at a time.");
        return;
    }

    member.setCreditBalance(member.getCreditBalance() + creditsToAdd);
    System.out.println("Credits added successfully. New balance: " + member.getCreditBalance());

    // Log the credit change in the credit history file
    logCreditHistory(member.getEmail(), creditsToAdd, (int) member.getCreditBalance()); // Explicit cast to int
}

    // Log credit history to file
private static void logCreditHistory(String memberEmail, int creditChange, int newBalance) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(CREDIT_HISTORY_FILE, true))) {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        bw.write(memberEmail + "," + date + "," + creditChange + "," + newBalance + "\n");
    } catch (IOException e) {
        System.out.println("Error saving credit history.");
    }
}

public static void viewCreditHistory() {
    if (member == null) {
        System.out.println("You need to be logged in as a member to view your credit history.");
        return;
    }

    File file = new File(CREDIT_HISTORY_FILE);
    if (!file.exists()) {
        System.out.println("No credit history available.");
        return;
    }

    System.out.println("Credit History for " + member.getFirstName() + " " + member.getLastName() + ":");

    try (BufferedReader br = new BufferedReader(new FileReader(CREDIT_HISTORY_FILE))) {
        String line;
        boolean historyFound = false;

        while ((line = br.readLine()) != null) {
            String[] historyData = line.split(",");
            if (historyData[0].equals(member.getEmail())) {
                System.out.println("Date: " + historyData[1] + ", Change: " + historyData[2] + ", New Balance: " + historyData[3]);
                historyFound = true;
            }
        }

        if (!historyFound) {
            System.out.println("No credit history found for this member.");
        }

    } catch (IOException e) {
        System.out.println("Error reading credit history file.");
    }
}




    // Update the member details back to the users.txt file
    private static void updateUserInFile(Member member) {
        try {
            List<String> usersData = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] userData = line.split(",");
                    if (userData[0].equals(member.getEmail())) {
                        // Update the user information
                        usersData.add(member.toString()); // Convert member data to string
                    } else {
                        usersData.add(line); // Keep existing user data
                    }
                }
            }

            // Write the updated users data back to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, false))) {
                for (String userData : usersData) {
                    bw.write(userData + "\n");
                }
            }

        } catch (IOException e) {
            System.out.println("Error updating users file.");
        }
    }


    // Simulated method to find a member by email from the users.txt file
// Simulated method to find a member by email from the users.txt file
private static Member findMemberByEmail(String email) {
    try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            // Use a more sophisticated splitting method that accounts for quotes around the address
            String[] userData = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            if (userData[0].equals(email)) {
                try {
                    // Remove quotes from address field if present
                    String address = userData[7].replaceAll("^\"|\"$", "");

                    // Parse the details to create a Member object
                    return new Member(
                        userData[0], // Email
                        userData[1], // Password
                        userData[2], // First Name
                        userData[3], // Last Name
                        userData[4], // Mobile
                        userData[5], // Date of Birth
                        userData[6], // Gender
                        address,     // Address (handle potential quotes)
                        Integer.parseInt(userData[8]), // Credit Balance (must be a valid integer)
                        new ArrayList<>() // Empty previous classes list
                    );
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing credit balance from file for user: " + email);
                    System.out.println("Credit balance found: " + userData[8]);
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading users file.");
    }
    return null;
}





    // Admin: Cancel Class
    public static void cancelClass(Scanner scanner) {
        System.out.print("Enter class name to cancel: ");
        String className = scanner.next();

        try (BufferedReader br = new BufferedReader(new FileReader(CLASSES_FILE))) {
            List<String> updatedClasses = new ArrayList<>();
            String line;
            boolean classCancelled = false;
            while ((line = br.readLine()) != null) {
                String[] classData = line.split(",");
                if (classData[0].equalsIgnoreCase(className)) {
                    classCancelled = true;
                    System.out.println("Class " + className + " canceled.");
                } else {
                    updatedClasses.add(line);
                }
            }
            if (!classCancelled) {
                System.out.println("Class not found.");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CLASSES_FILE, false))) {
                for (String updatedClass : updatedClasses) {
                    bw.write(updatedClass + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error canceling class.");
        }
    }

    // Feature: Admin Class Creation with Difficulty Level, Validation, and Instructor Details
    public static void createClass(Scanner scanner) {
        System.out.print("Enter class name (e.g., Yoga, Pilates): ");
        String className = scanner.next();

        System.out.print("Enter instructor's first name: ");
        String instructorFirstName = scanner.next();

        System.out.print("Enter instructor's last name: ");
        String instructorLastName = scanner.next();

        System.out.print("Enter instructor's date of birth (dd/MM/yyyy): ");
        String instructorDOB = scanner.next();

        System.out.print("Enter instructor's qualifications: ");
        String qualifications = scanner.next();

        System.out.print("Enter instructor's specializations: ");
        String specializations = scanner.next();

        System.out.print("Enter class date (dd/MM/yyyy): ");
        String classDate = scanner.next();

        System.out.print("Enter class time (HH:mm): ");
        String classTime = scanner.next();

        System.out.print("Enter class duration (45, 60, 90): ");
        int classDuration = scanner.nextInt();

        System.out.print("Enter class occurrence (e.g., Weekly, Monthly): ");
        String occurrence = scanner.next();

        System.out.print("Enter class credit points: ");
        int creditPoints = scanner.nextInt();

        System.out.print("Enter studio number (1-6): ");
        int studio = scanner.nextInt();
        int capacity = getStudioCapacity(studio);

        if (capacity == 0) {
            System.out.println("Invalid studio number.");
            return;
        }

        System.out.print("Enter class category (e.g., Yoga, Cardio): ");
        String category = scanner.next();

        System.out.print("Enter difficulty level (Beginner, Intermediate, Advanced): ");
        String difficultyLevel = scanner.next();

        if (!validateDifficultyLevel(difficultyLevel)) {
            System.out.println("Invalid difficulty level. Please choose from Beginner, Intermediate, Advanced.");
            return;
        }

        // Save class details to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CLASSES_FILE, true))) {
            bw.write(className + "," + instructorFirstName + " " + instructorLastName + "," + classDate + "," + classTime + "," +
                     classDuration + "," + occurrence + "," + creditPoints + "," + "Studio " + studio + "," + capacity + "," +
                     category + "," + difficultyLevel + "\n");
            System.out.println("Class created successfully.");
        } catch (IOException e) {
            System.out.println("Error saving class details.");
        }
    }

    // Helper method to validate difficulty level
    private static boolean validateDifficultyLevel(String difficultyLevel) {
        return difficultyLevel.equalsIgnoreCase("Beginner") || 
               difficultyLevel.equalsIgnoreCase("Intermediate") || 
               difficultyLevel.equalsIgnoreCase("Advanced");
    }


    public static void viewUpcomingClasses() {
    System.out.println("Upcoming Classes:");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date currentDate = new Date();  // Current date

    try (BufferedReader br = new BufferedReader(new FileReader(CLASSES_FILE))) {
        String line;
        boolean foundUpcomingClass = false;

        while ((line = br.readLine()) != null) {
            String[] classData = line.split(",");
            Date classDate = dateFormat.parse(classData[2]);  // Parse the class date
            
            // Check if the class is in the future
            if (classDate.after(currentDate)) {
                System.out.println("Class: " + classData[0] +
                        ", Instructor: " + classData[1] +
                        ", Date: " + classData[2] +
                        ", Time: " + classData[3] +
                        ", Location: " + classData[7]);
                foundUpcomingClass = true;
            }
        }

        if (!foundUpcomingClass) {
            System.out.println("No upcoming classes found.");
        }

    } catch (IOException | ParseException e) {
        System.out.println("Error reading class file or parsing date.");
    }
}




    // Feature: Filter Classes
    public static void filterClasses(Scanner scanner) {
        System.out.println("Filter by: 1. Date 2. Category 3. Difficulty Level 4. Instructor Name");
        int filterChoice = scanner.nextInt();
        System.out.print("Enter filter value: ");
        String filterValue = scanner.next();

        boolean matchFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CLASSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] classData = line.split(",");
                switch (filterChoice) {
                    case 1:
                        if (classData[2].equals(filterValue)) {
                            System.out.println("Class: " + classData[0] + ", Date: " + classData[2]);
                            matchFound = true;
                        }
                        break;
                    case 2:
                        if (classData[9].equalsIgnoreCase(filterValue)) {
                            System.out.println("Class: " + classData[0] + ", Category: " + classData[9]);
                            matchFound = true;
                        }
                        break;
                    case 3:
                        if (classData[10].equalsIgnoreCase(filterValue)) {
                            System.out.println("Class: " + classData[0] + ", Difficulty Level: " + classData[10]);
                            matchFound = true;
                        }
                        break;
                    case 4:
                        if (classData[1].equalsIgnoreCase(filterValue)) {
                            System.out.println("Class: " + classData[0] + ", Instructor: " + classData[1]);
                            matchFound = true;
                        }
                        break;
                    default:
                        System.out.println("Invalid filter option.");
                        return;
                }
            }

            if (!matchFound) {
                switch (filterChoice) {
                    case 1:
                        System.out.println("No class found for the entered date.");
                        break;
                    case 2:
                        System.out.println("No class found for the entered category.");
                        break;
                    case 3:
                        System.out.println("No class found for the entered difficulty level.");
                        break;
                    case 4:
                        System.out.println("No class found for the entered instructor name.");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading class file.");
        }
    }

    

     // Main Method
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    while (true) {
        System.out.println("Welcome to Monash Wellness System");
        if (!isLoggedIn) {
            System.out.println("1. Login");
            System.out.println("2. Exit");
        } else if (isLoggedIn && loggedInUser.equals("Admin")) {
            System.out.println("1. View Class Listings");
            System.out.println("2. Create a Class");
            System.out.println("3. Add Credits to Member");
            System.out.println("4. Cancel a Class");
            System.out.println("5. Logout");
        } else if (isLoggedIn && loggedInUser.equals("Member")) {
            System.out.println("1. View Class Listings");
            System.out.println("2. Book a Class");
            System.out.println("3. View Upcoming Classes");
            System.out.println("4. View Credit History");
            System.out.println("5. Cancel a Booking");
            System.out.println("6. View Credit Balance");
            System.out.println("7. Provide Feedback");
            System.out.println("8. Filter Classes");
            System.out.println("9. Logout");
        }

        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();

        if (!isLoggedIn) {
            if (choice == 1) {
                login(scanner);
            } else if (choice == 2) {
                System.out.println("Exiting system...");
                System.exit(0);
            }
        } else if (isLoggedIn && loggedInUser.equals("Admin")) {
            switch (choice) {
                case 1:
                    viewClassListings();
                    break;
                case 2:
                    createClass(scanner);
                    break;
                case 3:
                    addCreditsToMember(scanner);
                    break;
                case 4:
                    cancelClass(scanner);
                    break;
                case 5:
                    logout();
                    break;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        } else if (isLoggedIn && loggedInUser.equals("Member")) {
            switch (choice) {
                case 1:
                    viewClassListings();
                    break;
                case 2:
                    bookClass(scanner);
                    break;
                case 3:
                    viewUpcomingClasses();  // View Upcoming Classes
                    break;
                case 4:
                    viewCreditHistory();  // View Credit History
                    break;
                case 5:
                    cancelBooking(scanner);
                    break;
                case 6:
                    System.out.println("Your current credit balance: " + member.getCreditBalance());
                    break;
                case 7:
                    provideFeedback(scanner);
                    break;
                case 8:
                    filterClasses(scanner);
                    break;
                case 9:
                    logout();
                    break;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }
}
}
