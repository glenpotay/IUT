package model.dao;

import model.data.persistence.Besoin;
import model.data.persistence.Competence;
import model.data.persistence.DPS;
import model.data.persistence.Sport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class SportDAO that manages the database operations for the Sport entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class SportDAO {

    /**
     * Finds all sports in the database.
     *
     * @return a list of all Sport objects
     */
    public List<Sport> findAll() {
        List<Sport> sports = new ArrayList<>();

        try (Connection con = ConnectionBDD.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Sport")) {

            while (rs.next()) {
                Sport s = new Sport(
                        rs.getLong("CODE"),
                        rs.getString("NOM")
                );
                sports.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sports;
    }

    /**
     * Finds a Sport by its name.
     * @param sportName the name of the sport to search for
     * @return a Sport object if found, otherwise null
     */
    public Sport findByName(String sportName) {
        Sport ret = null;
        String query = "SELECT * FROM Sport WHERE NOM = ? ";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, sportName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ret = new Sport(rs.getLong("CODE"), sportName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Finds a Sport by its ID.
     *
     * @param id the ID of the sport to search for
     * @return a Sport object if found, otherwise null
     */
    public Sport findById(long id) {
        Sport ret = null;
        String query = "SELECT * FROM Sport WHERE ID = ? ";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ret = new Sport(id, rs.getString("NOM"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
