package model.dao;

import model.data.persistence.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class NotificationDAO that manages the database operations for the Notification entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class NotificationDAO {

    /**
     * Récupère toutes les notifications dont l'utilisateur est destinataire
     *
     * @param idRecipient l'id de l'utilisateur destinataire
     * @return la liste des notifications reçues
     */
    public ArrayList<Notification> findByIdSender(long idRecipient) {
        ArrayList<Notification> listNotifications = new ArrayList<>();
        String query = "SELECT * FROM Notification WHERE Notification.recipient = ? ORDER BY date DESC";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idRecipient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getString("date"),
                        rs.getLong("sender"),
                        rs.getLong("recipient"),
                        rs.getLong("idDPS"),
                        rs.getBoolean("isViewed")
                );
                listNotifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listNotifications;
    }

    /**
     * save save the notification in the database
     *
     * @param notification the notification to save
     */
    public void insert(Notification notification) {
        String query = "INSERT INTO Notification (title, message, date, sender, recipient, idDPS, isViewed) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getMessage());
            stmt.setString(3, notification.getDate());
            stmt.setLong(4, notification.getSender());
            stmt.setLong(5, notification.getRecipient());
            stmt.setLong(6, notification.getIdDPS());
            stmt.setBoolean(7, notification.getIsViewed());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Deletes a notification from the database.
     *
     * @param notification the notification
     */
    public void delete(Notification notification) {
        String query = "DELETE FROM Notification WHERE idDPS = ? AND sender = ? AND recipient = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {


            stmt.setLong(1, notification.getSender());
            stmt.setLong(2, notification.getRecipient());
            stmt.setLong(3, notification.getIdDPS());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
