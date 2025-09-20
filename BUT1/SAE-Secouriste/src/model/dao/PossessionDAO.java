package model.dao;

import model.data.persistence.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class PossessionDAO that manages the database operations for the Possession entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class PossessionDAO {

    /**
     * Inserts a new Possession into the database.
     *
     * @param possession the Possession to insert
     */
    public void insert(Possession possession) {
        String query = "INSERT INTO Possession (competence, secouriste) VALUES (?, ?)";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            for (Competence competence : possession.getCompetencesSec()) {
                stmt.setString(1, competence.getIntitule());
                stmt.setLong(2, possession.getSecouriste().getIdSecouriste());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Finds a Possession by its associated Secouriste.
     *
     * @param secouriste the Secouriste to search for
     * @return the Possession associated with the given Secouriste
     */
    public Possession find (Secouriste secouriste) {
        Possession ret = null;
        try (Connection con = ConnectionBDD.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Possession WHERE SECOURISTE = " + secouriste.getIdSecouriste() + "")) {
            ArrayList<Competence> competences = new ArrayList<>();
            while (rs.next()) {
                String intitule = rs.getString("Competence");

                competences.add(new Competence(intitule));
            }
            ret = new Possession(competences, secouriste);
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }
        return ret;
    }

    /**
     * Finds all competences possessed by a Secouriste.
     *
     * @param secouriste the Secouriste to search for
     * @return a Possession object containing the competences of the Secouriste
     */
    public Possession findCompetencesBySecouriste (Secouriste secouriste) {
        Possession ret = null;
        try (Connection con = ConnectionBDD.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Possession WHERE SECOURISTE = " + secouriste.getIdSecouriste() + "")) {
            ArrayList<Competence> competences = new ArrayList<>();
            while (rs.next()) {
                String intitule = rs.getString("Competence");

                competences.add(new Competence(intitule));
            }
            ret = new Possession(competences, secouriste);
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }
        return ret;
    }

    /**
     * Deletes a specific Possession record by Secouriste ID and Competence.
     *
     * @param idSecouriste the ID of the Secouriste
     * @param intituleCompetence the intitule of the Competence
     */
    public void deletePossession(long idSecouriste, String intituleCompetence) {
        String query = "DELETE FROM Possession WHERE secouriste = ? AND competence = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idSecouriste);
            stmt.setString(2, intituleCompetence);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all Possessions for a specific Secouriste.
     *
     * @param idSecouriste the ID of the Secouriste
     */
    public void deleteAllPossessionsForSecouriste(long idSecouriste) {
        String query = "DELETE FROM Possession WHERE secouriste = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, idSecouriste);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
