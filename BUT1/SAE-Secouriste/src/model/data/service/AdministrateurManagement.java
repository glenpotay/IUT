package model.data.service;

import javafx.scene.image.Image;
import model.dao.AdministrateurDAO;
import model.data.persistence.Administrateur;
import model.data.persistence.Secouriste;

import java.util.List;

/**
 * AdministrateurManagement class
 * This class manages the operations related to Administrateur.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class AdministrateurManagement {

    /**
     * Instance of AdministrateurDAO to interact with the database.
     */
    private final AdministrateurDAO administrateurDAO = new AdministrateurDAO();

    /**
     * Default constructor for AdministrateurManagement.
     */
    public List<Administrateur> getAdministrateurs() {
        return this.administrateurDAO.findAll();
    }

    /**
     * Method to get an Administrateur by its ID.
     *
     * @return the Administrateur object corresponding to the given ID.
     */
    public Administrateur getAdministrateurById(long idAdministrateur) {
        return administrateurDAO.findById(idAdministrateur);
    }

    /**
     * Method to update the photo of a Secouriste.
     * @param idSecouriste the ID of the Secouriste whose photo is to be updated.
     * @param image the new photo as a byte array.
     * @return true if the photo was successfully updated, false otherwise.
     */
    public boolean updatePhoto(long idSecouriste, byte[] image) {
        return administrateurDAO.insererPhoto(idSecouriste, image);
    }

    /**
     * Method to retrieve the photo of a Secouriste.
     * @param idSecouriste the ID of the Secouriste whose photo is to be retrieved.
     * @return the Image object representing the Secouriste's photo.
     */
    public Image recupererPhoto(long idSecouriste) {
        return administrateurDAO.recupererPhoto(idSecouriste);
    }

    /**
     * Method to add a new Administrateur.
     *
     * @param administrateur the Administrateur object to be added.
     */
    public void addAdministrateur(Administrateur administrateur) {
        administrateurDAO.insert(administrateur);
    }

    /**
     * Method to remove an existing Administrateur.
     * @param administrateur the Administrateur object to be removed.
     */
    public void removeAdministrateur(Administrateur administrateur) {
        this.administrateurDAO.delete(administrateur);
    }
}
