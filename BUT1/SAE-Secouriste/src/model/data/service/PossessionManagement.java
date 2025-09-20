package model.data.service;

import model.dao.PossessionDAO;
import model.data.persistence.Competence;
import model.data.persistence.Possession;
import model.data.persistence.Secouriste;

/**
 * This class manages possessions.
 * It provides methods to retrieve, add, and remove possessions for rescuers.
 *
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class PossessionManagement {

    /**
     * DAO instance for Possession operations.
     */
    private final PossessionDAO possessionDAO = new PossessionDAO();

    /**
     * Retrieves a Possession by the associated Secouriste.
     *
     * @param secouriste the Secouriste for which the possession is to be retrieved
     * @return the Possession instance associated with the given Secouriste
     */
    public Possession getPossessionBySecouriste(Secouriste secouriste) {
        return this.possessionDAO.find(secouriste);
    }

    /**
     * Adds a new Possession to the database.
     * @param possession the Possession instance to be added
     */
    public void addPossession(Possession possession) {
        this.possessionDAO.insert(possession);
    }

    /**
     * Removes a Possession for a specific Secouriste and Competence.
     *
     * @param secouriste the Secouriste from whom the possession is to be removed
     * @param competence the Competence associated with the possession to be removed
     */
    public void removePossession(Secouriste secouriste, Competence competence) {
        this.possessionDAO.deletePossession(secouriste.getIdSecouriste(), competence.getIntitule());
    }
}
