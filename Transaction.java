import java.util.Date;
import java.io.*;
import java.util.List;

/**
 * The Transaction class represents a financial transaction in the system,
 * such as a class booking or a refund. It stores details like transaction ID,
 * date, type (e.g., booking or refund), amount, and the member associated
 * with the transaction.
 * 
 * This class provides methods to get and set transaction details and
 * retrieve the transaction history of a member.
 * 
 * @see Member
 * @author Janvi
 */
public class Transaction {
    private String transactionId;
    private Date date;
    private String type; // e.g., "booking", "refund"
    private double amount;
    private Member member;

    /**
     * Constructor to initialize a Transaction object.
     * 
     * @param transactionId  The unique ID of the transaction
     * @param date           The date of the transaction
     * @param type           The type of transaction (e.g., "booking", "refund")
     * @param amount         The amount involved in the transaction
     * @param member         The member associated with the transaction
     */
    public Transaction(String transactionId, Date date, String type, 
                       double amount, Member member) {
        this.transactionId = transactionId;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.member = member;
    }

    // Getters and setters for transaction details

    /**
     * Gets the transaction ID.
     * 
     * @return The transaction ID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the date of the transaction.
     * 
     * @return The date of the transaction
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the transaction.
     * 
     * @param date The date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the type of transaction (e.g., "booking", "refund").
     * 
     * @return The type of transaction
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of transaction (e.g., "booking", "refund").
     * 
     * @param type The type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the amount involved in the transaction.
     * 
     * @return The transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount involved in the transaction.
     * 
     * @param amount The amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the member associated with the transaction.
     * 
     * @return The member involved in the transaction
     */
    public Member getMember() {
        return member;
    }

    /**
     * Sets the member associated with the transaction.
     * 
     * @param member The member to associate with the transaction
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Retrieves the transaction history for a given member.
     * 
     * @param member The member whose transaction history is to be retrieved
     * @return The transaction history of the member
     */
    public static List<Transaction> getHistory(Member member) {
        // For simplicity, return the member's transaction history
        return member.getTransactionHistory();
    }
}
