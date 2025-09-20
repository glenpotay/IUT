package model.data.service;

import java.util.ArrayList;
import java.util.List;

import model.dao.DPSDAO;
import model.data.persistence.DPS;
import model.data.persistence.Journee;
import model.data.persistence.Site;
import model.data.persistence.Sport;

/**
 * DPSManagement class
 * This class manages the operations related to DPS (Emergency Preparedness System).
 * It provides methods to add, retrieve, update, and delete DPS records.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DPSManagement {

    /**
     * Instance of DPSDAO to interact with the database.
     */
    private final DPSDAO dpsDAO = new DPSDAO();

    /**
     * Retrieves all DPS records from the database.
     */
    public List<DPS> getDps(){
        return this.dpsDAO.findAll();
    }

    /**
     * Adds a new DPS record to the database.
     * @param dps the DPS instance to be added
     */
    public void addDps(DPS dps) {
        this.dpsDAO.insert(dps);
    }

    /**
     * Checks if a DPS record exists in the database by its ID.
     * @param id the ID of the DPS record to check
     * @return true if the DPS exists, false otherwise
     */
    public boolean exists(long id) {
        return (this.dpsDAO.findById(id) != null);
    }

    /**
     * Retrieves a DPS record by its ID.
     * @param id the ID of the DPS record to retrieve
     * @return the DPS instance with the specified ID, or null if not found
     */
    public DPS getDpsById(long id) {
        return this.dpsDAO.findById(id);
    }

    /**
     * Retrieves a DPS record by its name.
     * @param nom the name of the DPS record to retrieve
     * @return the DPS instance with the specified name, or null if not found
     */
    public DPS getDpsByName(String nom) {
        return this.dpsDAO.findByName(nom);
    }

    /**
     * Get the number of DPS records in the database.
     * @return the count of DPS records
     */
    public long numberOfDps(){
        return this.dpsDAO.findAll().size();
    }

    /**
     * Retrieves a list of all DPS names from the database.
     * @return an ArrayList of DPS names
     */
    public ArrayList<String> getDpsName() {
        return this.dpsDAO.findDPSName();
    }

    /**
     * Removes a DPS record from the database.
     * @param dps the DPS instance to be removed
     */
    public void removeDps(DPS dps) {
        this.dpsDAO.deleteByIdDPS(dps.getId());
    }

    /**
     * Retrieves all DPS records associated with a specific site.
     * @param idDay the ID of the day for which to retrieve DPS records
     * @return an ArrayList of DPS records for the specified day
     */
    public ArrayList<DPS> getDpsByDay(long idDay) {
        return this.dpsDAO.findByDay(idDay);
    }

    /**
     * Updates an existing DPS record in the database.
     * @param dps the DPS instance to be updated
     */
    public void updateDps(DPS dps) {
        this.dpsDAO.updateDps(dps);
    }
}
