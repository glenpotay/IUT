package model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.data.persistence.Competence;

import static model.dao.ConnectionBDD.getConnection;

/**
 * Class CompetenceDAO that manages the database operations for the Competence entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class CompetenceDAO {

    /**
     * Finds all competence intitules in the database.
     *
     * @return a list of competence intitules
     */
    public ArrayList<String> findAllIntitule() {
        ArrayList<String> competences = new ArrayList<>();

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Competence")) {

            while (rs.next()) {
                String intitule = rs.getString("intitule");
                competences.add(intitule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competences;
    }

    /**
     * Finds all Competence objects in the database.
     *
     * @return a list of Competence objects
     */
    public ArrayList<Competence> findAll() {
        ArrayList<Competence> competences = new ArrayList<>();

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Competence")) {

            while (rs.next()) {
                Competence competence = new Competence(rs.getString("intitule"));
                competences.add(competence);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competences;
    }
}
