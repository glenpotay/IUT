package model.dao;

import javafx.scene.image.Image;
import model.data.persistence.Administrateur;
import model.data.persistence.Administrateur;
import model.data.persistence.Secouriste;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static model.dao.ConnectionBDD.getConnection;

/**
 * Class AdministrateurDAO that manages the database operations for the Administrateur entity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class AdministrateurDAO {

    /**
     * Inserts a new Administrateur into the database.
     *
     * @param administrateur the Administrateur to insert
     */
    public void insert(Administrateur administrateur) {
        String query = "INSERT INTO Administrateur (idAdministrateur, nom, prenom, date_naissance, adresse, tel) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, administrateur.getIdAdministrateur());
            stmt.setString(2, administrateur.getNom());
            stmt.setString(3, administrateur.getPrenom());
            stmt.setString(4, administrateur.getDateNaissance());
            stmt.setString(5, administrateur.getAdresse());
            stmt.setString(6, administrateur.getTel());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all Administrateurs in the database.
     */
    public List<Administrateur> findAll() {
        List<Administrateur> administrateurs = new ArrayList<>();

        try (Connection con = ConnectionBDD.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Administrateur")) {

            while (rs.next()) {
                Administrateur s = new Administrateur(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("dateNaissance"),
                        rs.getString("tel"),
                        rs.getString("adresse"),
                        rs.getBytes("photo")
                );
                administrateurs.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return administrateurs;
    }

    /**
     * findById find the adminstrator by his id
     *
     * @return return the administrator
     */
    public Administrateur findById(long idAdministrateur) {
        String query = "SELECT * FROM Administrateur WHERE Administrateur.idAdministrateur = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setLong(1, idAdministrateur);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Administrateur administrateur = new Administrateur(
                        rs.getLong("idAdministrateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("date_naissance"),
                        rs.getString("tel"),
                        rs.getString("adresse"),
                        rs.getBytes("photo")
                );
                return administrateur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a photo for an existing Administrateur.
     * @param idAdministrateur the ID of the Administrateur
     * @param photoBytes the byte array representing the photo
     * @return true if the photo was successfully inserted, false otherwise
     */
    public boolean insererPhoto(long idAdministrateur, byte[] photoBytes) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Administrateur SET photo = ? WHERE idAdministrateur = ?")) {

            stmt.setBytes(1, photoBytes);
            stmt.setLong(2, idAdministrateur);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * recuperer une photo pour un Administrateur existant
     *
     * @param id - id du Administrateur
     * @return la photo de profil
     */
    public Image recupererPhoto(long id) {
        String sql = "SELECT photo FROM Administrateur WHERE idAdministrateur = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                InputStream is = rs.getBinaryStream("photo");
                if (is != null) {
                    return new Image(is);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Delete a specific Administrateur from the database.
     *
     * @param administrateur the Administrateur to delete
     */
    public void delete(Administrateur administrateur) {
        System.out.println("ID à supprimer : " + administrateur.getIdAdministrateur());
        String query = "DELETE FROM Secouriste WHERE idAdministrateur = ?";
        try (Connection con = ConnectionBDD.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setLong(1, administrateur.getIdAdministrateur());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}