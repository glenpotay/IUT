package model.data.service;

import model.dao.SportDAO;
import model.data.persistence.Sport;

import java.util.ArrayList;
import java.util.List;

/**
 * SportManagement class
 * This class manages the operations related to Sport.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class SportManagement {

    /**
     * Instance of SportDAO to interact with the database.
     */
    private final SportDAO sportDAO = new SportDAO();

    /**
     * Retrieves all sports from the database.
     * @return
     */
    public List<Sport> getSports(){
        return this.sportDAO.findAll();
    }

    /**
     * Retrieves the sport by its ID.
     * @param id the ID of the sport to retrieve
     * @return the Sport instance with the specified ID, or null if not found
     */
    public Sport getSport(int id){
        return this.sportDAO.findById(id);
    }

    /**
     * Retrieves a list of sports by their name
     * @param name the name of the sport to search for
     * @return a list of Sport instances that match the given name
     */
    public Sport getSportByName(String name){
        return this.sportDAO.findByName(name);
    }
}
