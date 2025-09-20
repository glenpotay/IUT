package model.dao;

import model.data.persistence.Disponibilite;
import model.data.persistence.Journee;
import model.data.persistence.Secouriste;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class DisponibiliteDAO that manages the database operations for the Disponibilite entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DisponibiliteDAO {

    /**
     * Finds all Disponibilite records for a given Secouriste.
     * @param secouriste the Secouriste for whom to find Disponibilite records
     * @return an ArrayList of Disponibilite objects associated with the given Secouriste
     */
    public ArrayList<Disponibilite> findById(Secouriste secouriste) {
        ArrayList<Disponibilite> disponibilites = new ArrayList<>();
        long id = secouriste.getIdSecouriste();
        String query = "SELECT * FROM Disponibilite JOIN Journee ON Journee.id = journeeDisp WHERE secouristeDisp = ?";
        try (Connection con = ConnectionBDD.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                disponibilites.add(new Disponibilite(secouriste, new Journee(rs.getInt("Jour"), rs.getInt("Mois"), rs.getInt("Annee"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disponibilites;
    }

    /**
     * Deletes Disponibilite records by the specified date.
     * @param jour the day of the month
     * @param mois the month
     * @param annee the year
     */
    public void deleteByJour(int jour, int mois, int annee) {
        String query = "DELETE FROM Disponibilite WHERE jour = ? AND mois = ? AND annee = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, jour);
            stmt.setInt(2, mois);
            stmt.setInt(3, annee);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a specific Disponibilite record by Secouriste ID and Journee ID.
     * @param idSecouriste the ID of the Secouriste
     * @param idJournee the ID of the Journee
     */
    public void deleteDisponibilite(long idSecouriste, long idJournee) {
        String query = "DELETE FROM Disponibilite WHERE secouristeDisp = ? AND journeeDisp = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idSecouriste);
            stmt.setLong(2, idJournee);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all Disponibilite records for a given Secouriste.
     * @param idSecouriste the ID of the Secouriste
     */
    public void deleteAllDisponibilites(long idSecouriste) {
        String query = "DELETE FROM Disponibilite WHERE secouristeDisp = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idSecouriste);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new Disponibilite record into the database.
     * @param idSecouriste the ID of the Secouriste
     * @param idJournee the ID of the Journee
     */
    public void insert(long idSecouriste, long idJournee) {
        String insertDisponibilite = "INSERT INTO Disponibilite (secouristeDisp, journeeDisp) VALUES (?, ?)";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmtInsert = con.prepareStatement(insertDisponibilite)) {

            stmtInsert.setLong(1, idSecouriste);
            stmtInsert.setLong(2, idJournee);
            stmtInsert.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
