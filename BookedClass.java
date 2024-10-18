import java.util.ArrayList;
import java.util.List;

/**
 * The BookedClass class represents a class that has been booked by a member.
 * It includes details about the class such as the class name, instructor,
 * date, time, location, duration, category, and difficulty level.
 * It also tracks the class capacity, credit points, attendance status,
 * and feedback given by members.
 */
public class BookedClass {
    // Class details
    private String className;
    private Instructor instructor;
    private String date;
    private String time;
    private int duration;
    private String location;
    private int capacity;
    private String category;
    private String difficultyLevel;

    // Additional class properties
    private String attendanceStatus;
    private int creditPoints;
    private List<Feedback> feedbackList;

    public BookedClass(String className, Instructor instructor, String date, 
                       String time, int duration, String location, int capacity, 
                       String category, String difficultyLevel, int creditPoints) {
        this.className = className;
        this.instructor = instructor;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.location = location;
        this.capacity = capacity;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.attendanceStatus = "Attended";
        this.creditPoints = creditPoints;
        this.feedbackList = new ArrayList<>();
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public boolean checkAvailability() {
        return capacity > 0;
    }

    public void addFeedback(Feedback feedback) {
        this.feedbackList.add(feedback);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public List<Feedback> getFeedback() {
        return feedbackList;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedbackList = feedback;
    }
}
