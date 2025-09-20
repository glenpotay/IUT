package model.data.service;

import model.dao.BesoinDAO;
import model.data.persistence.Besoin;
import model.data.persistence.Competence;
import model.data.persistence.DPS;

/**
 * This class manages the needs (Besoin) in the system.
 * It provides methods to add, update, retrieve, and delete needs associated with DPS (Dossier de Prise en Charge).
 *
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class BesoinManagement {

    /**
     * Instance of BesoinDAO to interact with the database for Besoin operations.
     */
    private final BesoinDAO besoinDAO = new BesoinDAO();

    /**
     * Adds a new besoin (need) to the database.
     * @param besoin
     */
    public void addBesoin(Besoin besoin) {
        this.besoinDAO.insert(besoin);
    }

    /**
     * Retrieves a Besoin by its associated DPS (Dossier de Prise en Charge).
     *
     * @param dps the DPS for which the Besoin is to be retrieved
     * @return the Besoin associated with the given DPS, or null if not found
     */
    public Besoin getBesoinByDPS(DPS dps) {
        return this.besoinDAO.findByDPS(dps);
    }

    /**
     * Met à jour un besoin dans la base de données en supprimant les anciennes compétences
     * et en insérant les nouvelles.
     *
     * @param besoin Le besoin à mettre à jour
     */
    public void updateBesoin(Besoin besoin) {
        if (besoin == null) {
            return;
        }

        // Supprimer l'ancien besoin
        this.besoinDAO.deleteByDps(besoin.getDps().getId());

        // S'il reste des compétences à pourvoir, on réinsère le besoin
        if (!besoin.getCompetences().isEmpty()) {
            this.besoinDAO.insert(besoin);
        }
    }

    /**
     * Retrieves the ID of a Besoin by its associated DPS and Competence.
     *
     * @param dps the DPS for which the Besoin is to be retrieved
     * @param competence the Competence associated with the Besoin
     * @return the ID of the Besoin if found, otherwise -1
     */
    public long getIdBesoinByDPSAndCompetence(DPS dps, Competence competence) {
        return this.besoinDAO.findByDPSAndCompetence(dps, competence);
    }

    /**
     * Deletes a Besoin by its associated DPS and Competence.
     *
     * @param dps the DPS for which the Besoin is to be deleted
     * @param competence the Competence associated with the Besoin to be deleted
     */
    public void deleteBesoinByDPSAndCompetence(DPS dps, Competence competence) {
        this.besoinDAO.deleteByDPSAndCompetence(dps, competence);
    }

    /**
     * Deletes all Besoins associated with a specific DPS.
     *
     * @param dps the DPS for which all associated Besoins are to be deleted
     */
    public void removeByDps(DPS dps) {
        besoinDAO.deleteByDps(dps.getId());
    }

    /**
     * Deletes a specific Besoin from the database.
     *
     * @param besoin the Besoin instance to be deleted
     */
    public void removeBesoin(Besoin besoin) {
        this.besoinDAO.deleteBesoin(besoin);
    }
}
