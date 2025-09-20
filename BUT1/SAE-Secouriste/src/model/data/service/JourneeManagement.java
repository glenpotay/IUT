package model.data.service;

import model.dao.JourneeDAO;
import model.data.persistence.Journee;

import java.util.List;

/**
 * JourneeManagement class
 * This class manages the operations related to Journee.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class JourneeManagement {

    /**
     * Instance of JourneeDAO to interact with the database.
     */
    private final JourneeDAO journeeDAO =  new JourneeDAO();

    /**
     * Retrieves a Journee by its ID.
     * @param jour the day of the Journee
     * @param mois the month of the Journee
     * @param annee the year of the Journee
     * @return the ID of the Journee if found, otherwise -1
     */
    public long getJourneeByJour(int jour, int mois, int annee) {
        long id = this.journeeDAO.findIdByJour(jour, mois, annee);
        if (id == -1) {
            addJournee(new Journee(jour, mois, annee));
            id = getJourneeByJour(jour, mois, annee);
        }
        return id;
    }

    /**
     * Adds a new Journee to the database.
     * @param journee the Journee object to be added
     */
    public void addJournee(Journee journee) {
        this.journeeDAO.insert(journee);
    }
}
