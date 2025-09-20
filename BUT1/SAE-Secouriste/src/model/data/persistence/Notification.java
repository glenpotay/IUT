package model.data.persistence;

import java.util.Date;

/**
 * This class represents a notification in the system.
 * It contains details such as title, message, date, sender, recipient, and whether it has been viewed.
 *
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class Notification {

    /**
     * the title of the notification
     */
    private String title;
    private String message;
    private String date;
    private long sender;
    private long recipient;
    private long idDps;
    private boolean getIsViewed;

    /**
     * Constructor for Notification class.
     *
     * @param title the title of the notification
     * @param message the message of the notification
     * @param date the date of the notification
     * @param sender the ID of the sender
     * @param recipient the ID of the recipient
     * @param idDps the ID of the DPS associated with this notification
     * @param isViewed whether the notification has been viewed or not
     */
    public Notification(String title, String message, String date, long sender, long recipient, long idDps, boolean isViewed) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.sender = sender;
        this.recipient = recipient;
        this.idDps = idDps;
        this.getIsViewed = isViewed;
    }

    /**
     * Get the title of the notification.
     * @return the title of the notification
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the message of the notification.
     * @return the message of the notification
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the date of the notification.
     * @return the date of the notification
     */
    public String getDate() {
        return date;
    }

    /**
     * Get the ID of the DPS associated with this notification.
     * @return the ID of the DPS
     */
    public long getIdDPS() {
        return idDps; // Assuming recipient is the ID of the DPS
    }

    /**
     * Get the ID of the sender of the notification.
     * @return the ID of the sender
     */
    public long getSender() {
        return sender;
    }

    /**
     * Get the ID of the recipient of the notification.
     * @return the ID of the recipient
     */
    public long getRecipient() {
        return recipient;
    }

    /**
     * Get whether the notification has been viewed.
     * @return true if the notification has been viewed, false otherwise
     */
    public boolean getIsViewed() {
        return getIsViewed;
    }
}
