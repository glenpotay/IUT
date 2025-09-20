package model.data.service;

import javafx.scene.image.Image;
import model.dao.DAOFactory;
import model.dao.SecouristeDAO;
import model.data.persistence.Secouriste;
import model.data.persistence.User;

import java.io.File;
import java.util.List;

/**
 * SecouristeManagement class
 * This class manages the operations related to Secouriste.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class SecouristeManagement {

    /**
     * Instance of SecouristeManagement.
     */
    private static final SecouristeManagement instance = new SecouristeManagement();

    /**
     * DAO instance for Secouriste operations.
     * This instance is used to interact with the database for Secouriste-related operations.
     */
    private final SecouristeDAO secouristeDAO = new SecouristeDAO();

    /**
     * Retrieves all Secouristes from the database.
     * @return a List of Secouriste objects representing all Secouristes in the database
     */
    public List<Secouriste> getSecouristes() {
        return this.secouristeDAO.findAll();
    }

    /**
     * Retrieves a Secouriste by its ID.
     * @param idSecouriste the ID of the Secouriste to retrieve
     * @return the Secouriste object corresponding to the given ID
     */
    public Secouriste getSecouristeById(long idSecouriste) {
        return secouristeDAO.findById(idSecouriste);
    }

    /**
     * Finds all Secouristes in the database.
     * @return a List of all Secouriste objects
     */
    public List<Secouriste> findAll() {
        return secouristeDAO.findAll();
    }

    /**
     * Finds Secouristes by their associated Journee ID.
     * @param idJournee the ID of the Journee for which Secouristes are to be retrieved
     * @return a List of Secouriste objects associated with the given Journee ID
     */
    public List<Secouriste> findByIdJournee(long idJournee) {
        return secouristeDAO.findByDay(idJournee);
    }

    /**
     * Updates the photo of a Secouriste.
     * @param idSecouriste the ID of the Secouriste whose photo is to be updated
     * @param image the new photo as a byte array
     * @return true if the photo was successfully updated, false otherwise
     */
    public boolean updatePhoto(long idSecouriste, byte[] image) {
        return secouristeDAO.insererPhoto(idSecouriste, image);
    }

    /**
     * Retrieves the photo of a Secouriste.
     * @param idSecouriste the ID of the Secouriste whose photo is to be retrieved
     * @return the Image object representing the Secouriste's photo
     */
    public Image recupererPhoto(long idSecouriste) {
        return secouristeDAO.recupererPhoto(idSecouriste);
    }

    /**
     * Removes a Secouriste from the database.
     * @param secouriste the Secouriste object to be removed
     */
    public void removeSecouriste(Secouriste secouriste) {
        this.secouristeDAO.delete(secouriste);
    }

    /**
     * Retrieves a Secouriste by its User object.
     * @param secouriste the Secouriste whose User is to be retrieved
     * @return the User object associated with the given Secouriste
     */
    public User getUserBySecouriste(Secouriste secouriste) {
        return this.secouristeDAO.findUserBySecouriste(secouriste);
    }

    /**
     * Gets instance of SecouristeManagement.
     * @return an instance of SecouristeManagement
     */
    public static SecouristeManagement getInstanceSecouristeManagement() {
        return instance;
    }
}
