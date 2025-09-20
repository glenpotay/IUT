package model.data.service;

import model.dao.AffectationDAO;
import model.dao.DPSDAO;
import model.dao.NotificationDAO;
import model.data.persistence.DPS;
import model.data.persistence.Notification;

import java.util.ArrayList;

/**
 * This class manages notifications in the system.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class NotificationManagement {

    /**
     * instance of DPSDao
     */
    private final DPSDAO dpsDAO = new DPSDAO();

    /**
     * instance of NotificationManagement
     */
    private final NotificationDAO notificationDAO = new NotificationDAO();

    /**
     * instance of AffectationDAO
     */
    private final AffectationDAO affectationDAO = new AffectationDAO();

    /**
     * instance of AuthentificationManagement
     */
    private final AuthentificationManagement authentificationManagement = AuthentificationManagement.getInstanceAuthentificationManagement();

    /**
     * Constructor for NotificationManagement
     * Initializes the DAO instances to interact with the database.
     */
    public NotificationManagement() {
    }

    /**
     * Retrieves all notifications.
     *
     * @return a list of all notifications
     */
    public ArrayList<Notification> getNotificationByIdSender(long id) {
        return notificationDAO.findByIdSender(id);
    }


    /**
     * Creates a notification and saves it to the database.
     *
     * @param title                the title of the notification
     * @param message              the message of the notification
     * @param date                 the date of the notification
     * @param recipientDPSGroupName the name of the DPS group to which the notification is sent
     */
    public void createNotification(String title, String message, String date ,String recipientDPSGroupName) {

        long sender = authentificationManagement.getCurrentUser().getIdUser();

        // Research the DPS by name with the value of the comboBox
        DPS dps = dpsDAO.findByName(recipientDPSGroupName);


        long iDDps = dps.getId();

        System.out.println("Creating notification for DPS group: " + recipientDPSGroupName);
        System.out.println("DPS ID: " + iDDps);
        System.out.println("1Creating notification for sender message: " + message);
        System.out.println("Creating notification for sender date: " + date);

        // Get the list of rescuer IDs associated with the DPS group
        ArrayList<Integer> listIdRescuer = affectationDAO.findIdRescuerByDPS(iDDps);

        boolean isViewed = false;

        // Create a notification for each rescuer in the DPS group
        for(int idRescuer : listIdRescuer) {
            Notification notification = new Notification(title, message, date, sender, idRescuer, iDDps, isViewed);
            System.out.println("Insertion notification avec idDPS = " + notification.getIdDPS());
            notificationDAO.insert(notification);
        }
        System.out.println("Notification created for sender: " + sender);
        // Create a notification for the sender as well

        System.out.println("Creating notification for sender message: " + message);
        System.out.println("Creating notification for sender date: " + date);

        Notification notification = new Notification(title, message, date, sender, sender, iDDps, true);
        notificationDAO.insert(notification);
        System.out.println("Notification created for recipient: " + sender);
    }

    /**
     * Deletes a notification by its ID.
     * @param Notification the Notification instance to be deleted
     */
    public void deleteNotification(Notification Notification) {
        notificationDAO.delete(Notification);
    }
}

