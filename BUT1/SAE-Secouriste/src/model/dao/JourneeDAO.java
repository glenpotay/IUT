package model.dao;

import model.data.persistence.Journee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class JourneeDAO that manages the database operations for the Journee entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class JourneeDAO {

    /**
     * Inserts a new Journee into the database.
     *
     * @param journee the Journee to insert
     */
    public void insert(Journee journee) {
        String query = "INSERT INTO Journee (jour, mois, annee) VALUES (?, ?, ?)";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, journee.getJour());
            stmt.setLong(2, journee.getMois());
            stmt.setLong(3, journee.getAnnee());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the ID of a Journee by its date.
     *
     * @param jour the day of the Journee
     * @param mois the month of the Journee
     * @param annee the year of the Journee
     * @return the ID of the Journee, or -1 if not found
     */
    public long findIdByJour(int jour, int mois, int annee) {
        String query = "SELECT id FROM Journee WHERE jour = ? AND mois = ? AND annee = ?";
        long ret = -1;
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, jour);
            stmt.setInt(2, mois);
            stmt.setInt(3, annee);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ret =  rs.getLong("id");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
