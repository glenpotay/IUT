package model.data.service;

import model.dao.SiteDAO;
import model.data.persistence.Site;

import java.util.List;

/**
 * SiteManagement class
 * This class manages the operations related to Site.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class SiteManagement {

    /**
     * Instance of SiteDAO to interact with the database.
     */
    private final SiteDAO siteDAO = new SiteDAO();

    /**
     * Retrieves all sites from the database.
     * @return a List of Site objects representing all sites
     */
    public List<Site> getSites(){
        return this.siteDAO.findAll();
    }

    /**
     * Retrieves a Site by its name.
     * @param name the name of the site to be retrieved
     * @return the Site object with the specified name, or null if not found
     */
    public Site getSiteByName(String name){
        return this.siteDAO.findByName(name);
    }

    /**
     * Retrieves a Site by its ID.
     * @param id the ID of the site to be retrieved
     * @return the Site object with the specified ID, or null if not found
     */
    public Site getSiteById(int id){
        return this.siteDAO.findById(id);
    }
}
