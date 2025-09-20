package model.data.persistence;

/**
 * This class represents a necessity.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class Necessite {
    private Competence comp1;
    private Competence comp2;

    /**
     * Constructor
     * @param comp1 - first competence
     * @param comp2 - second competence
     */
    public Necessite(Competence comp1, Competence comp2) {
        this.comp1 = comp1;
        this.comp2 = comp2;
    }
    /**
     * Getter for comp1.
     * @return comp1
     */
    public Competence getComp1() {
        return comp1;
    }

    /**
     * Setter for comp1.
     * @param comp1
     */
    public void setComp1(Competence comp1) {
        this.comp1 = comp1;
    }


    /**
     * Getter for comp2.
     * @return comp2
     */
    public Competence getComp2() {
        return comp2;
    }

    /**
     * Setter for comp2.
     * @param comp2
     */
    public void setComp2(Competence comp2) {
        this.comp2 = comp2;
    }
}
