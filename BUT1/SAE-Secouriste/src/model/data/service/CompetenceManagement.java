package model.data.service;

import model.dao.CompetenceDAO;
import model.data.persistence.Competence;

import java.util.ArrayList;

/**
 * This class manages competences.
 * It provides methods to retrieve all competences from the database.
 *
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class CompetenceManagement {

    /**
     * DAO instance for Competence operations.
     */
    private final CompetenceDAO competenceDAO = new CompetenceDAO();

    /**
     * Retrieves all competences from the database.
     *
     * @return an ArrayList of strings representing the names of all competences
     */
    public ArrayList<String> getCompetences() {
        return this.competenceDAO.findAllIntitule();
    }
}
