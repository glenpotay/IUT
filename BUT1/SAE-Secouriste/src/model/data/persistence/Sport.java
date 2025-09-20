package model.data.persistence;

/**
 * Sport class
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class Sport {

    /**
     * Informations du sport : code et nom.
     */
    long code; // Code du sport
    String nom;  // Nom du sport

    /**
     * Constructor of Sport
     * @param code - code of Sport
     * @param nom - name of the sport
     */
    public Sport(long code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    /**
     * Getter of Code
     * @return the code of the sport
     */
    public long getCode() {
        return this.code;
    }

    /**
     * Getter of Nom
     * @return the name of the sport
     */
    public String getNom() {
        return this.nom;
    }
}
