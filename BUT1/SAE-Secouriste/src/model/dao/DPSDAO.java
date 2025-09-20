package model.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.ScatterChart;
import model.data.persistence.DPS;
import model.data.persistence.Site;
import model.data.persistence.Sport;
import model.data.persistence.Journee;

/**
 * Class DPSDAO that manages the database operations for the DPS entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DPSDAO {

    /**
     * Inserts a new DPS into the database, creating a new Journee if it does not exist.
     *
     * @param dps the DPS to insert
     */
    public void insert(DPS dps) {
        String checkJourneeQuery = "SELECT id FROM Journee WHERE jour = ? AND mois = ? AND annee = ?";
        String insertJourneeQuery = "INSERT INTO Journee (jour, mois, annee) VALUES (?, ?, ?)";
        String insertDPSQuery = "INSERT INTO DPS (id, name, horaire_depart, horaire_fin, site, sport, journee) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionBDD.getConnection()) {
            // Commencer transaction
            con.setAutoCommit(false);

            int journeeId;

            // D'abord, vérifier si la journée existe déjà
            try (PreparedStatement checkStmt = con.prepareStatement(checkJourneeQuery)) {
                checkStmt.setInt(1, dps.getJournee().getJour());
                checkStmt.setInt(2, dps.getJournee().getMois());
                checkStmt.setInt(3, dps.getJournee().getAnnee());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        // La journée existe déjà, récupérer son ID
                        journeeId = rs.getInt("id");
                    } else {
                        // La journée n'existe pas, la créer
                        try (PreparedStatement insertStmt = con.prepareStatement(insertJourneeQuery, Statement.RETURN_GENERATED_KEYS)) {
                            insertStmt.setInt(1, dps.getJournee().getJour());
                            insertStmt.setInt(2, dps.getJournee().getMois());
                            insertStmt.setInt(3, dps.getJournee().getAnnee());
                            insertStmt.executeUpdate();

                            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    journeeId = generatedKeys.getInt(1);
                                } else {
                                    con.rollback();
                                    throw new SQLException("Échec de la récupération de l'ID de la journée.");
                                }
                            }
                        }
                    }
                }
            }

            // Insertion dans DPS
            try (PreparedStatement stmt2 = con.prepareStatement(insertDPSQuery)) {
                stmt2.setLong(1, dps.getId());
                stmt2.setString(2, dps.getName());
                stmt2.setInt(3, dps.getHoraireDepart());
                stmt2.setInt(4, dps.getHoraireFin());
                stmt2.setLong(5, dps.getSite().getCode());
                stmt2.setLong(6, dps.getSport().getCode());
                stmt2.setInt(7, journeeId);
                stmt2.executeUpdate();
            }

            // Commit transaction
            con.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Finds all DPS records in the database.
     *
     * @return a list of all DPS records
     */
    public ArrayList<DPS> findAll () {
        ArrayList<DPS> dpsList = new ArrayList<>();
        try (Connection con = ConnectionBDD.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT d.ID, d.NAME, d.HORAIRE_DEPART, d.HORAIRE_FIN, j.JOUR, j.MOIS, j.ANNEE, s.CODE AS SITE_CODE, s.NOM AS SITE_NOM, s.LONGITUDE AS SITE_LON, s.LATITUDE AS SITE_LAT, sp.CODE AS SPORT_CODE, sp.NOM AS SPORT_NOM FROM DPS d JOIN Site s ON d.SITE = s.CODE JOIN Sport sp ON d.SPORT = sp.CODE JOIN Journee j ON j.ID = d.JOURNEE ORDER BY j.ANNEE, j.MOIS, j.JOUR, d.HORAIRE_DEPART")) {
             while (rs.next()) {
                 // DPS
                 int id = rs.getInt("ID");
                 String name = rs.getString("NAME");
                 int horaireDepart = rs.getInt("HORAIRE_DEPART");
                 int horaireFin = rs.getInt("HORAIRE_FIN");

                 // Site
                 long siteCode = rs.getLong("SITE_CODE");
                 String siteNom = rs.getString("SITE_NOM");
                 Float siteLongitude = rs.getFloat("SITE_LON");
                 Float siteLatitude = rs.getFloat("SITE_LAT");
                 Site site = new Site(siteCode, siteNom, siteLongitude, siteLatitude); // adapte ce constructeur

                 // Sport
                 Long sportCode = rs.getLong("SPORT_CODE");
                 String sportNom = rs.getString("SPORT_NOM");
                 Sport sport = new Sport(sportCode, sportNom); // idem, adapte selon ton modèle

                 // Journee
                 int jour = rs.getInt("JOUR");
                 int mois = rs.getInt("MOIS");
                 int annee = rs.getInt("ANNEE");
                 Journee journee = new Journee(jour, mois, annee);

                 // DPS complet
                 DPS dps = new DPS(id, name, horaireDepart, horaireFin, site, sport, journee);
                 dpsList.add(dps);
             }
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }
        return dpsList;
    }

    /**
     * Finds a DPS by its ID.
     *
     * @param idDps the ID of the DPS to find
     * @return the DPS with the specified ID, or null if not found
     */
    public DPS findById(long idDps) {
        DPS ret = null;
        String query = "SELECT d.ID, d.NAME, d.HORAIRE_DEPART, d.HORAIRE_FIN, j.JOUR, j.MOIS, j.ANNEE, s.CODE AS SITE_CODE, s.NOM AS SITE_NOM, s.LONGITUDE AS SITE_LON, s.LATITUDE AS SITE_LAT, sp.CODE AS SPORT_CODE, sp.NOM AS SPORT_NOM FROM DPS d JOIN Site s ON d.SITE = s.CODE JOIN Sport sp ON d.SPORT = sp.CODE JOIN Journee j ON j.ID = d.JOURNEE WHERE d.ID = ?";
        try (Connection con = ConnectionBDD.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1,idDps);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // DPS
                String name = rs.getString("NAME");
                int horaireDepart = rs.getInt("HORAIRE_DEPART");
                int horaireFin = rs.getInt("HORAIRE_FIN");

                // Site
                long siteCode = rs.getLong("SITE_CODE");
                String siteNom = rs.getString("SITE_NOM");
                Float siteLongitude = rs.getFloat("SITE_LON");
                Float siteLatitude = rs.getFloat("SITE_LAT");
                Site site = new Site(siteCode, siteNom, siteLongitude, siteLatitude); // adapte ce constructeur

                // Sport
                Long sportCode = rs.getLong("SPORT_CODE");
                String sportNom = rs.getString("SPORT_NOM");
                Sport sport = new Sport(sportCode, sportNom); // idem, adapte selon ton modèle

                // Journee
                int jour = rs.getInt("JOUR");
                int mois = rs.getInt("MOIS");
                int annee = rs.getInt("ANNEE");
                Journee journee = new Journee(jour, mois, annee);

                ret = new DPS(idDps, name, horaireDepart, horaireFin, site, sport, journee);;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Finds all DPS names in the database.
     *
     * @return a list of all DPS names
     */
    public ArrayList<String> findDPSName() {
        ArrayList<String> dpsNames = new ArrayList<>();
        String query = "SELECT NAME FROM DPS";
        try (Connection con = ConnectionBDD.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                dpsNames.add(rs.getString("NAME"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dpsNames;
    }

    /**
     * Finds a DPS by its name.
     *
     * @param name the name of the DPS to find
     * @return the DPS with the specified name, or null if not found
     */
    public DPS findByName(String name) {
        DPS ret = null;
        String query = "SELECT d.ID, d.NAME, d.HORAIRE_DEPART, d.HORAIRE_FIN, j.JOUR, j.MOIS, j.ANNEE, s.CODE AS SITE_CODE, s.NOM AS SITE_NOM, s.LONGITUDE AS SITE_LON, s.LATITUDE AS SITE_LAT, sp.CODE AS SPORT_CODE, sp.NOM AS SPORT_NOM FROM DPS d JOIN Site s ON d.SITE = s.CODE JOIN Sport sp ON d.SPORT = sp.CODE JOIN Journee j ON j.ID = d.JOURNEE WHERE d.NAME = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("ID");
                int horaireDepart = rs.getInt("HORAIRE_DEPART");
                int horaireFin = rs.getInt("HORAIRE_FIN");

                long siteCode = rs.getLong("SITE_CODE");
                String siteNom = rs.getString("SITE_NOM");
                float siteLongitude = rs.getFloat("SITE_LON");
                float siteLatitude = rs.getFloat("SITE_LAT");
                Site site = new Site(siteCode, siteNom, siteLongitude, siteLatitude);

                long sportCode = rs.getLong("SPORT_CODE");
                String sportNom = rs.getString("SPORT_NOM");
                Sport sport = new Sport(sportCode, sportNom);

                int jour = rs.getInt("JOUR");
                int mois = rs.getInt("MOIS");
                int annee = rs.getInt("ANNEE");
                Journee journee = new Journee(jour, mois, annee);

                ret = new DPS(id, name, horaireDepart, horaireFin, site, sport, journee);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Deletes a DPS by its ID.
     *
     * @param idDps the ID of the DPS to delete
     */
    public void deleteByIdDPS(long idDps) {
        String query = "DELETE FROM DPS WHERE ID = ?";
        try (Connection con = ConnectionBDD.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, idDps);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Finds all DPS records for a specific day.
     *
     * @param idDay the ID of the day to find DPS records for
     * @return a list of DPS records for the specified day
     */
    public ArrayList<DPS> findByDay(long idDay) {
        ArrayList<DPS> dpsList = new ArrayList<>();
        String query = "SELECT d.ID, d.NAME, d.HORAIRE_DEPART, d.HORAIRE_FIN, j.JOUR, j.MOIS, j.ANNEE, s.CODE AS SITE_CODE, s.NOM AS SITE_NOM, s.LONGITUDE AS SITE_LON, s.LATITUDE AS SITE_LAT, sp.CODE AS SPORT_CODE, sp.NOM AS SPORT_NOM FROM DPS d JOIN Site s ON d.SITE = s.CODE JOIN Sport sp ON d.SPORT = sp.CODE JOIN Journee j ON j.ID = d.JOURNEE WHERE d.JOURNEE = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, idDay);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // DPS
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int horaireDepart = rs.getInt("HORAIRE_DEPART");
                int horaireFin = rs.getInt("HORAIRE_FIN");

                // Site
                long siteCode = rs.getLong("SITE_CODE");
                String siteNom = rs.getString("SITE_NOM");
                Float siteLongitude = rs.getFloat("SITE_LON");
                Float siteLatitude = rs.getFloat("SITE_LAT");
                Site site = new Site(siteCode, siteNom, siteLongitude, siteLatitude); // adapte ce constructeur

                // Sport
                Long sportCode = rs.getLong("SPORT_CODE");
                String sportNom = rs.getString("SPORT_NOM");
                Sport sport = new Sport(sportCode, sportNom); // idem, adapte selon ton modèle

                // Journee
                int jour = rs.getInt("JOUR");
                int mois = rs.getInt("MOIS");
                int annee = rs.getInt("ANNEE");
                Journee journee = new Journee(jour, mois, annee);

                // DPS complet
                DPS dps = new DPS(id, name, horaireDepart, horaireFin, site, sport, journee);
                dpsList.add(dps);
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return dpsList;
    }

    /**
     * Updates an existing DPS in the database.
     * If the associated Journee does not exist, it will be created.
     *
     * @param dps the DPS to update
     */
    public void updateDps(DPS dps) {
        String updateQuery = "UPDATE DPS SET NAME = ?, HORAIRE_DEPART = ?, HORAIRE_FIN = ?, SITE = ?, SPORT = ?, JOURNEE = ? WHERE ID = ?";
        String checkJourneeQuery = "SELECT id FROM Journee WHERE jour = ? AND mois = ? AND annee = ?";
        String insertJourneeQuery = "INSERT INTO Journee (jour, mois, annee) VALUES (?, ?, ?)";

        try (Connection con = ConnectionBDD.getConnection()) {
            con.setAutoCommit(false); // Début de transaction

            int journeeId;

            // Vérifie si la journée existe
            try (PreparedStatement checkStmt = con.prepareStatement(checkJourneeQuery)) {
                checkStmt.setInt(1, dps.getJournee().getJour());
                checkStmt.setInt(2, dps.getJournee().getMois());
                checkStmt.setInt(3, dps.getJournee().getAnnee());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        journeeId = rs.getInt("id");
                    } else {
                        try (PreparedStatement insertStmt = con.prepareStatement(insertJourneeQuery, Statement.RETURN_GENERATED_KEYS)) {
                            insertStmt.setInt(1, dps.getJournee().getJour());
                            insertStmt.setInt(2, dps.getJournee().getMois());
                            insertStmt.setInt(3, dps.getJournee().getAnnee());
                            insertStmt.executeUpdate();

                            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    journeeId = generatedKeys.getInt(1);
                                } else {
                                    con.rollback();
                                    throw new SQLException("Échec de la récupération de l'ID de la journée pour mise à jour.");
                                }
                            }
                        }
                    }
                }
            }

            // Mise à jour de la ligne DPS
            try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
                stmt.setString(1, dps.getName());
                stmt.setInt(2, dps.getHoraireDepart());
                stmt.setInt(3, dps.getHoraireFin());
                stmt.setLong(4, dps.getSite().getCode());
                stmt.setLong(5, dps.getSport().getCode());
                stmt.setInt(6, journeeId);
                stmt.setLong(7, dps.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    con.rollback();
                    throw new SQLException("La mise à jour de DPS a échoué, aucun enregistrement affecté.");
                }
            }

            con.commit(); // Fin de transaction
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
