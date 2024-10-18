/**
 * The Feedback class represents feedback given by a user for a class or
 * service. It includes a comment, a rating, and whether the feedback was
 * given anonymously.
 * 
 * The class provides getters and setters for each field, allowing
 * the feedback information to be accessed and modified as needed.
 * 
 * @author Aryan & Neeraj
 */
public class Feedback {
    // Feedback details
    private String comment;
    private int rating;
    private boolean anonymous;

    /**
     * Constructor to initialize a Feedback object.
     * 
     * @param comment    The feedback comment provided by the user
     * @param rating     The rating provided by the user (e.g., 1 to 5)
     * @param anonymous  Whether the feedback is anonymous
     */
    public Feedback(String comment, int rating, boolean anonymous) {
        this.comment = comment;
        this.rating = rating;
        this.anonymous = anonymous;
    }

    // Getters and setters for each field

    /**
     * Gets the comment provided by the user.
     * 
     * @return The feedback comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment for the feedback.
     * 
     * @param comment The feedback comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the rating provided by the user.
     * 
     * @return The feedback rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating for the feedback.
     * 
     * @param rating The rating to set (e.g., 1 to 5)
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Checks if the feedback was given anonymously.
     * 
     * @return True if the feedback is anonymous, false otherwise
     */
    public boolean isAnonymous() {
        return anonymous;
    }

    /**
     * Sets whether the feedback is anonymous.
     * 
     * @param anonymous True to make the feedback anonymous, false otherwise
     */
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}
