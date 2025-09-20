package model.data.service;
import model.dao.DisponibiliteDAO;
import model.data.persistence.Disponibilite;
import model.data.persistence.Journee;
import model.data.persistence.Secouriste;

import java.util.ArrayList;

/**
 * DisponibiliteManagement class
 * This class manages the operations related to Disponibilite.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DisponibiliteManagement {

    /**
     * Instance of DisponibiliteDAO to interact with the database.
     */
    private final DisponibiliteDAO disponibiliteDAO = new DisponibiliteDAO();

    /**
     * Instance of JourneeManagement to manage Journee operations.
     */
    private final JourneeManagement journeeManagement = new JourneeManagement();

    /**
     * Default constructor for DisponibiliteManagement.
     */
    public ArrayList<Disponibilite> getDisponibilites(Secouriste secouriste) {
        return this.disponibiliteDAO.findById(secouriste);
    }

    /**
     * Method to remove a Disponibilite for a given Secouriste and Journee.
     *
     * @param secouriste the Secouriste whose Disponibilite is to be removed.
     * @param journee the Journee for which the Disponibilite is to be removed.
     */
    public void removeDisponibilite(Secouriste secouriste, Journee journee) {
        this.disponibiliteDAO.deleteDisponibilite(secouriste.getIdSecouriste(), new JourneeManagement().getJourneeByJour(journee.getJour(), journee.getMois(), journee.getAnnee()));
    }

    /**
     * Method to add a Disponibilite for a given Secouriste and Journee.
     *
     * @param secouriste the Secouriste for whom the Disponibilite is to be added.
     * @param journee the Journee for which the Disponibilite is to be added.
     */
    public void addDisponibilite(Secouriste secouriste, Journee journee) {
        long idJournee = journeeManagement.getJourneeByJour(journee.getJour(), journee.getMois(), journee.getAnnee());
        if (idJournee == -1) {
            System.err.println("Erreur : Journee non trouvée pour ajout de disponibilité.");
            return;
        }
        disponibiliteDAO.insert(secouriste.getIdSecouriste(), idJournee);
    }
}
