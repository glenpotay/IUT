package model.data.persistence;

import model.data.persistence.Site;
import model.data.persistence.Sport;
import model.data.persistence.Journee;
/**
 * DPS (Emergency Preparedness System)
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DPS {

    /**
     * DPS id
     */
    private long id;

    /**
     * Name DPS
     */
    private String name;

    /**
     * Start time
     */
    private int horaireDepart;

    /**
     * End time
     */
    private int horaireFin;

    /**
     * Place of the DPS
     */
    private Site site;

    /**
     * Sport of the DPS
     */
    private Sport sport;

    /**
     * Day of the DPS
     */
    private Journee journee;

    /**
     * Constructor of DPS
     * @param id - id of DPS
     * @param horaireDepart - start hour
     * @param horaireFin - finish hour
     * @param site - site of DPS
     * @param sport - sport of DPS
     * @param journee - day of DPS
     */
    public DPS(long id, String name, int horaireDepart, int horaireFin, Site site, Sport sport, Journee journee) {
        this.id = id;
        this.name = name;
        this.horaireDepart = horaireDepart;
        this.horaireFin = horaireFin;
        this.site = site;
        this.sport = sport;
        this.journee = journee;
    }

    /**
     * Get the DPS id
     * @return the DPS id
     */
    public long getId() {
        return this.id;
    }

    /**
     * Set the DPS id
     * @param id the DPS id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the DPS name
     * @return the DPS name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the DPS name
     * @param name the DPS name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the start time
     * @return the start time
     */
    public int getHoraireDepart() {
        return this.horaireDepart;
    }

    /**
     * Set the start time
     * @param horaireDepart the start time
     */
    public void setHoraireDepart(int horaireDepart) {
        this.horaireDepart = horaireDepart;
    }

    /**
     * Get the end time
     * @return the end time
     */
    public int getHoraireFin() {
        return this.horaireFin;
    }

    /**
     * Set the end time
     * @param horaireFin the end time
     */
    public void setHoraireFin(int horaireFin) {
        this.horaireFin = horaireFin;
    }

    /**
     * Get the place of the DPS
     * @return the place of the DPS
     */
    public Site getSite() {
        return this.site;
    }

    /**
     * Set the place of the DPS
     * @param site the place of the DPS
     */
    public void setSite(Site site) {
        this.site = site;
    }

    /**
     * Get the sport of the DPS
     * @return the sport of the DPS
     */
    public Sport getSport() {
        return this.sport;
    }

    /**
     * Set the sport of the DPS
     * @param sport the sport of the DPS
     */
    public void setSport(Sport sport) {
        this.sport = sport;
    }

    /**
     * Get the day of the DPS
     * @return the day of the DPS
     */
    public Journee getJournee() {
        return this.journee;
    }

    /**
     * Set the day of the DPS
     * @param journee the day of the DPS
     */ 
    public void setJournee(Journee journee) {
        this.journee = journee;
    }

    /**
     * Override of the equals method to compare DPS based on their id
     * @param o the object to compare with
     * @return true if the DPS are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DPS dps = (DPS) o;
        return id == dps.id; // ou getIdDps() si idDps est privé
    }
}
