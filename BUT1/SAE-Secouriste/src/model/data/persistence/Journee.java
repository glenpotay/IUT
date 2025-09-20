package model.data.persistence;

/**
 * Day
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class Journee {

    /**
     * Day
     */
    private int jour;

    /**
     * Month
     */
    private int mois;

    /**
     * Year
     */
    private int annee;

    /**
     * Constructor of Journee
     * @param jour - the day
     * @param mois - the month
     * @param annee - the year
     */
    public Journee(int jour, int mois, int annee) {
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }
    /**
     * Get the day
     * @return the day
     */
    public int getJour() {
        return this.jour;
    }

    /**
     * Set the day
     * @param jour the day
     */
    public void setJour(int jour) {
        this.jour = jour;
    }

    /**
     * Get the month
     * @return the month
     */
    public int getMois() {
        return this.mois;
    }

    /**
     * Set the month
     * @param mois the month
     */
    public void setMois(int mois) {
        this.mois = mois;
    }

    /**
     * Get the year
     * @return the year
     */
    public int getAnnee() {
        return this.annee;
    }

    /**
     * Set the year
     * @param annee the year
     */
    public void setAnnee(int annee) {
        this.annee = annee;
    }

    /**
     * Override toString method to return the date in a readable format
     * @return the date as a string in the format "day month year"
     */
    @Override
    public String toString() {
        String[] months = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" };
        return this.jour + " " + months[this.mois - 1] + " " + this.annee;
    }



// For the exhaustive assignment algorithm, we need to override equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Journee journee = (Journee) o;

        // On compare tous les attributs pertinents
        return jour == journee.jour &&
               mois == journee.mois &&
               annee == journee.annee;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + jour;
        result = 31 * result + mois;
        result = 31 * result + annee;
        return result;
    }
}
