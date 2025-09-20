package model.dao;

import model.data.persistence.Affectation;
import model.data.persistence.Competence;
import model.data.persistence.Secouriste;
import model.data.service.DPSManagement;
import model.data.service.SecouristeManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Class AffectationDAO that manages the database operations for the Affectation entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class AffectationDAO {

    /**
     * Inserts a new Affectation into the database.
     *
     * @param affectation the Affectation to insert
     */
    public void insert(Affectation affectation) {
        String query = "INSERT INTO Affectation VALUES (?, ?, ?)";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, affectation.getSecouristeAffect().getIdSecouriste());
            stmt.setLong(2, affectation.getDPSAffect().getId());
            stmt.setString(3, affectation.getCompetenceAffect().getIntitule());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a rescuer is assigned to a specific day.
     *
     * @param idJournee the ID of the day
     * @param idSecouriste the ID of the rescuer
     * @return true if the rescuer is assigned, false otherwise
     */
    public boolean rescuerThisDay(long idJournee, long idSecouriste) {
        boolean ret = false;
        String query = "SELECT 1 FROM Affectation a JOIN DPS d ON a.DPSAffect = d.id WHERE d.journee = ? AND a.secouristeAffect = ? LIMIT 1";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idJournee);
            stmt.setLong(2, idSecouriste);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ret = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            ret = true;
        }
        return ret;
    }

    /**
     * Checks if an Affectation already exists in the database.
     *
     * @param affectation the Affectation to check
     * @return true if the Affectation exists, false otherwise
     */
    public boolean exists(Affectation affectation) {
        String query = "SELECT 1 FROM affectation WHERE secouristeAffect = ? AND DPSAffect = ? AND competenceAffect = ?";
        boolean ret = false;

        try (Connection con = ConnectionBDD.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, affectation.getSecouristeAffect().getIdSecouriste());
            stmt.setLong(2, affectation.getDPSAffect().getId());
            stmt.setString(3, affectation.getCompetenceAffect().getIntitule());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ret = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Finds all rescuers assigned to a specific DPS.
     *
     * @param idDPS the ID of the DPS
     * @return a list of IDs of rescuers assigned to the DPS
     */
    public ArrayList<Integer> findIdRescuerByDPS(long idDPS) {
        ArrayList<Integer> idSecouristes = new ArrayList<>();
        String query = "SELECT secouristeAffect FROM Affectation WHERE DPSAffect = ?";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idDPS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                idSecouristes.add(rs.getInt("secouristeAffect"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idSecouristes;
    }

    /**
     * Finds all Affectations for a specific rescuer.
     *
     * @param idRescuer the ID of the rescuer
     * @return a list of Affectations for the rescuer
     */
    public List<Affectation> findByRescuer(long idRescuer) {
        List<Affectation> affectations = new ArrayList<>();
        String query = "SELECT * FROM Affectation WHERE SecouristeAffect = ?";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idRescuer);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Object[]> save = new ArrayList<>();
            while (rs.next()) {
                long idSecouristeAffect = rs.getLong("secouristeAffect");
                long idDPS = rs.getLong("DPSAffect");
                String idCompetenceAffect = rs.getString("competenceAffect");
                save.add(new Object[]{idSecouristeAffect, idDPS, idCompetenceAffect});
            }
            for (Object[] row : save) {
                long idSecouristeAffect = (long) row[0];
                long idDPS = (long) row[1];
                String idCompetenceAffect = (String) row[2];

                Affectation affectation = new Affectation(
                        new SecouristeManagement().getSecouristeById(idSecouristeAffect),
                        new DPSManagement().getDpsById(idDPS),
                        new Competence(idCompetenceAffect)
                );
                affectations.add(affectation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

/**
     * Finds all Affectations for a specific DPS.
     *
     * @param idDps the ID of the DPS
     * @return a list of Affectations for the DPS
     */
    public List<Affectation> findByDPS(long idDps) {
        List<Affectation> affectations = new ArrayList<>();
        String query = "SELECT * FROM Affectation WHERE DpsAffect = ?";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idDps);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Object[]> save = new ArrayList<>();
            while (rs.next()) {
                long idSecouristeAffect = rs.getLong("secouristeAffect");
                long idDPS = rs.getLong("DPSAffect");
                String idCompetenceAffect = rs.getString("competenceAffect");
                save.add(new Object[]{idSecouristeAffect, idDPS, idCompetenceAffect});
            }
            for (Object[] row : save) {
                long idSecouristeAffect = (long) row[0];
                long idDPS = (long) row[1];
                String idCompetenceAffect = (String) row[2];

                Affectation affectation = new Affectation(
                        new SecouristeManagement().getSecouristeById(idSecouristeAffect),
                        new DPSManagement().getDpsById(idDPS),
                        new Competence(idCompetenceAffect)
                );
                affectations.add(affectation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

    /**
     * Deletes an Affectation from the database.
     *
     * @param affectation the Affectation to delete
     */
    public void delete(Affectation affectation) {
        String query = "DELETE FROM Affectation WHERE SecouristeAffect = ? AND DpsAffect = ? AND CompetenceAffect = ?";

        try (Connection con = ConnectionBDD.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, affectation.getSecouristeAffect().getIdSecouriste());
            stmt.setLong(2, affectation.getDPSAffect().getId());
            stmt.setString(3, affectation.getCompetenceAffect().getIntitule());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compte le nombre d'utilisations de chaque compétence dans les affectations.
     *
     * @return Une Map avec le nom de la compétence comme clé et le nombre d'utilisations comme valeur
     */
    public Map<String, Integer> countCompetencesUsage() {
        Map<String, Integer> usageCount = new HashMap<>();
        String query = "SELECT competence, COUNT(*) as count FROM Affectation GROUP BY competence ORDER BY count DESC";

        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String competenceIntitule = rs.getString("competence");
                int count = rs.getInt("count");
                usageCount.put(competenceIntitule, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usageCount;
    }
}
