package model.data.persistence;

import java.util.ArrayList;

/**
 * This class stores the number of someone that we need for DPS
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
*/
public class Besoin {

    /**
     * Private variable containing the dps
     */
    private DPS dps;

    /**
     * Private variable containing the competences
     */
    private ArrayList<Competence> competences;

    /**
     * Constructor of Besoin class
     * @param dps - DPS that he needs skills
     * @param competences - Skills needed
     */
    public Besoin(DPS dps, ArrayList<Competence> competences) {
        if (competences == null || dps == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        this.competences = competences;
        this.dps = dps;
    }

    /**
     * Get the competences needed for the DPS
     * @return the list of competences
     */
    public ArrayList<Competence> getCompetences() {
        return this.competences;
    }

    /**
     * Set the competences needed for the DPS
     * @param competences - the list of competences to set
     */
    public void setCompetences(ArrayList<Competence> competences) {
        this.competences = competences;
    }

    /**
     * Get the DPS that needs skills
     * @return the DPS instance
     */
    public DPS getDps() {
        return this.dps;
    }

    /**
     * Set the DPS that needs skills
     * @param dps - the DPS to set
     */
    public void setDps(DPS dps) {
        this.dps = dps;
    }
}
