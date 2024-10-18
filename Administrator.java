import java.io.*;
import java.util.*;

/**
 * The Administrator class extends the User class and provides additional 
 * functionality specifically for administrators, such as creating, updating, 
 * and deleting classes, managing member credits, and handling class cancellations.
 * 
 * <p>Inherits basic user attributes and methods from the User class, but 
 * implements admin-specific features like class management.</p>
 * 
 * @author Janvi
 */
public class Administrator extends User {
    
    /**
     * Unique identifier for the administrator.
     */
    @SuppressWarnings("unused")
    private String adminID;

    /**
     * Constructor for the Administrator class.
     * 
     * @param email        Administrator's email
     * @param password     Administrator's password
     * @param firstName    Administrator's first name
     * @param lastName     Administrator's last name
     * @param mobileNumber Administrator's mobile number
     * @param adminID      Administrator's unique ID
     */
    public Administrator(String email, String password, String firstName, String lastName, String mobileNumber, String adminID) {
        super(email, password, firstName, lastName, mobileNumber); // Calling the User class constructor
        this.adminID = adminID; // Assigning adminID
    }

    /**
     * Loads the administrator-specific file. 
     * Currently, this is a placeholder implementation.
     * Override from the abstract User class.
     */
    @Override
    public void loadFile() {
        System.out.println("Admin-specific file loading logic goes here.");
    }

    /**
     * Allows the administrator to create a new class.
     * Prompts the user for class details such as name, instructor, date, time, etc.
     * and writes this information to a file.
     * 
     * @param scanner Scanner object to receive input from the user
     */
    public void createClass(Scanner scanner) {
        System.out.print("Enter Class Name: ");
        String className = scanner.next();
        System.out.print("Enter Instructor Name: ");
        String instructor = scanner.next();
        System.out.print("Enter Date (dd/mm/yyyy): ");
        String date = scanner.next();
        System.out.print("Enter Time (hh:mm): ");
        String time = scanner.next();
        System.out.print("Enter Duration (in minutes): ");
        String duration = scanner.next();
        System.out.print("Enter Capacity: ");
        int capacity = scanner.nextInt();

        // Writing class information to "classes.txt" file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("classes.txt", true))) {
            bw.write(className + "," + instructor + "," + date + "," + time + "," + duration + ",Weekly,500,Location," + capacity + ",Category,Beginner\n");
            System.out.println("Class created successfully.");
        } catch (IOException e) {
            System.out.println("Error creating class.");
        }
    }

    /**
     * Allows the administrator to update details of an existing class.
     * Prompts for the class name and new details (date, time, capacity), and updates
     * the corresponding entry in the "classes.txt" file.
     * 
     * @param scanner Scanner object to receive input from the user
     */
    public void updateClass(Scanner scanner) {
        System.out.print("Enter the name of the class to update: ");
        String className = scanner.next();
        System.out.print("Enter new Date (dd/mm/yyyy): ");
        String newDate = scanner.next();
        System.out.print("Enter new Time (hh:mm): ");
        String newTime = scanner.next();
        System.out.print("Enter new Capacity: ");
        int newCapacity = scanner.nextInt();

        // Temporary list to store updated class information
        List<String> updatedClasses = new ArrayList<>();
        boolean classFound = false;

        // Reading existing class data from "classes.txt"
        try (BufferedReader br = new BufferedReader(new FileReader("classes.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] classData = line.split(",");
                // Check if the current line matches the class to be updated
                if (classData[0].equalsIgnoreCase(className)) {
                    classFound = true;
                    line = className + "," + classData[1] + "," + newDate + "," + newTime + "," + classData[4] + ",Weekly,500," + classData[7] + "," + newCapacity + ",Category,Beginner";
                    System.out.println("Class updated successfully.");
                }
                updatedClasses.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error updating class: " + e.getMessage());
        }

        // If class is found, save the updated class data back to the file
        if (classFound) {
            saveUpdatedClasses(updatedClasses);
        } else {
            System.out.println("Class not found.");
        }
    }

    /**
     * Allows the administrator to delete an existing class.
     * Prompts for the class name and removes the corresponding entry from the "classes.txt" file.
     * 
     * @param scanner Scanner object to receive input from the user
     */
    public void deleteClass(Scanner scanner) {
        System.out.print("Enter the class name to delete: ");
        String className = scanner.next();
        List<String> updatedClasses = new ArrayList<>();
        boolean classFound = false;

        // Reading existing class data from "classes.txt"
        try (BufferedReader br = new BufferedReader(new FileReader("classes.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] classData = line.split(",");
                // If the class name matches, skip adding it to the updated list
                if (!classData[0].equalsIgnoreCase(className)) {
                    updatedClasses.add(line);
                } else {
                    classFound = true;
                    System.out.println("Class deleted successfully.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error deleting class.");
        }

        // If class is found, save the updated class data back to the file
        if (classFound) {
            saveUpdatedClasses(updatedClasses);
        } else {
            System.out.println("Class not found.");
        }
    }

    /**
     * Adds credits to a member's account.
     * Ensures that the credits added do not exceed 1000.
     * 
     * @param scanner Scanner object to receive input from the user
     */
    public void addCredits(Scanner scanner) {
        System.out.print("Enter Member Email: ");
        String memberEmail = scanner.next();
        System.out.print("Enter amount of credits to add (Max 1000): ");
        int creditsToAdd = scanner.nextInt();

        if (creditsToAdd <= 1000) {
            // Logic to find the member and add credits (Placeholder)
            System.out.println("Credits added successfully.");
        } else {
            System.out.println("You cannot add more than 1000 credits.");
        }
    }

    /**
     * Cancels a scheduled class and processes refunds for all members who booked it.
     * 
     * @param scanner Scanner object to receive input from the user
     */
    public void cancelClass(Scanner scanner) {
        System.out.print("Enter Class Name to cancel: ");
        String className = scanner.next();
        // Logic to read 'bookings.txt' and process refunds (Placeholder)
        System.out.println("Class canceled and refunds processed.");
    }

    /**
     * Saves the updated class information to the "classes.txt" file.
     * 
     * @param updatedClasses List of updated class entries to be saved
     */
    private void saveUpdatedClasses(List<String> updatedClasses) {
        // Writing updated class data back to the "classes.txt" file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("classes.txt"))) {
            for (String updatedClass : updatedClasses) {
                bw.write(updatedClass + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving updated classes: " + e.getMessage());
        }
    }

    /**
     * Administrator login method (currently not implemented).
     */
    @Override
    public void login() {
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    /**
     * Administrator logout method (currently not implemented).
     */
    @Override
    public void logout() {
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    /**
     * View administrator profile method (currently not implemented).
     */
    @Override
    public void viewProfile() {
        throw new UnsupportedOperationException("Unimplemented method 'viewProfile'");
    }
}
