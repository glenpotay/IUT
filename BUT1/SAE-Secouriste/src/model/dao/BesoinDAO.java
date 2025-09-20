package model.dao;

import model.data.persistence.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class BesoinDAO that manages the database operations for the Besoin entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class BesoinDAO {

    /**
     * Inserts a new Besoin into the database.
     *
     * @param besoin the Besoin to insert
     */
    public void insert(Besoin besoin) {
        for (Competence competence : besoin.getCompetences()) {
            String query = "INSERT INTO Besoin (DPS, COMPETENCE) VALUES (?,?)";
            try (Connection con = ConnectionBDD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {
                 stmt.setLong(1, besoin.getDps().getId());
                 stmt.setString(2, competence.getIntitule());
                 stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Finds a Besoin by its associated DPS.
     *
     * @param dps the DPS to search for
     * @return the Besoin associated with the given DPS
     */
    public Besoin findByDPS(DPS dps) {
        Besoin ret = null;
        String query = "SELECT * FROM Besoin WHERE DPS = ? ";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, dps.getId());
            ResultSet rs = stmt.executeQuery();

            ArrayList<Competence> competences = new ArrayList<>();
            while (rs.next()) {
                String intitule = rs.getString("COMPETENCE");
                competences.add(new Competence(intitule));
            }
            ret = new Besoin(dps, competences);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Finds a Besoin by its associated DPS and Competence.
     *
     * @param dps the DPS to search for
     * @param competence the Competence to search for
     * @return the ID of the Besoin associated with the given DPS and Competence, or -1 if not found
     */
    public long findByDPSAndCompetence(DPS dps, Competence competence) {
        long ret = -1;
        String query = "SELECT ID FROM Besoin WHERE DPS = ? AND COMPETENCE = ? LIMIT 1";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, dps.getId());
            stmt.setString(2, competence.getIntitule());
            ResultSet rs = stmt.executeQuery();

            ArrayList<Competence> competences = new ArrayList<>();
            if (rs.next()) {
                ret = rs.getLong("ID");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Deletes a Besoin by its associated DPS and Competence.
     *
     * @return a list of all Besoins
     */
    public void deleteByDPSAndCompetence(DPS dps, Competence competence) {
        long idBesoin = findByDPSAndCompetence(dps, competence);
        String query = "DELETE FROM Besoin WHERE ID = ?";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idBesoin);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes all Besoins associated with a specific DPS.
     *
     * @param idDps the ID of the DPS whose Besoins should be deleted
     */
    public void deleteByDps(long idDps) {
        String query = "DELETE FROM Besoin WHERE DPS = ?";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idDps);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes a specific Besoin from the database.
     *
     * @param besoin the Besoin to delete
     */
    public void deleteBesoin(Besoin besoin) {
        for (Competence competence : besoin.getCompetences()) {
            String query = "DELETE FROM Besoin WHERE DPS = ? AND COMPETENCE = ? LIMIT 1";

            try (Connection con = ConnectionBDD.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {

                stmt.setLong(1, besoin.getDps().getId());
                stmt.setString(2, competence.getIntitule());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
