package model.data.service;

import model.dao.NecessiteDAO;
import model.data.persistence.Necessite;

import java.util.List;

/**
 * This class manages the operations related to Necessite.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class NecessiteManagement {

    /**
     * Instance of NecessiteDAO to interact with the database.
     */
    private final NecessiteDAO necessiteDAO =  new NecessiteDAO();

    /**
     * Retrieves all Necessites from the database.
     * @return a List of Necessite objects representing all necessites
     */
    public List<Necessite> getNecessites() {
        return this.necessiteDAO.findAll();
    }

    /**
     * Method to remove a Necessite.
     * @param necessite the Necessite to be removed
     */
    public void removeNecessite(Necessite necessite) {
        this.necessiteDAO.deleteNecessite(necessite);
    }

    /**
     * Checks if a Necessite can be created.
     * @param necessite the Necessite to check
     * @return true if the Necessite can be created, false otherwise
     */
    public boolean isCreate(Necessite necessite) {
        return this.necessiteDAO.isCreate(necessite);
    }

    /**
     * Adds a new Necessite to the database.
     * @param newNecessite the Necessite to be added
     */
    public void addNecessite(Necessite newNecessite) {
        this.necessiteDAO.insertNecessite(newNecessite);
    }
}
