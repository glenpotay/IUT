package model.data.persistence;

/**
 * Availability of a rescuer
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class Disponibilite {

    /**
     * Rescuer available
     */
    private Secouriste secouristeDisp;

    /**
     * Day available
     */
    private Journee jourDisp;

    /**
     * Constructor of Disponibilite
     * @param secouriste - a rescuer
     * @param jour - her disponibility
     */
    public Disponibilite(Secouriste secouriste, Journee jour) {
        this.secouristeDisp = secouriste;
        this.jourDisp = jour;
    }

    /**
     * Get the rescuer available
     * @return the rescuer available
     */
    public Secouriste getSecouristeDisp() {
        return this.secouristeDisp;
    }

    /**
     * Set the rescuer available
     * @param secouristeDisp the rescuer available
     */
    public void setSecouristeDisp(Secouriste secouristeDisp) {
        this.secouristeDisp = secouristeDisp;
    }

    /**
     * Get the day available
     * @return the day available
     */
    public Journee getJourDisp() {
        return this.jourDisp;
    }

    /**
     * Set the day available
     * @param jourDisp the day available
     */
    public void setJourDisp(Journee jourDisp) {
        this.jourDisp = jourDisp;
    }
}
