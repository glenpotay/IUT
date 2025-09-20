package model.data.persistence;

/**
 * Site class
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class Site {

    /**
     * Informations du site : code, nom et coordonnées géographiques.
     */
    private long code;       // Code du site
    private String nom;        // Nom du site
    private float longitude;   // Longitude du site
    private float latitude;    // Latitude du site

    /**
     * Constructor of Site
     * @param code - id of Site
     * @param nom - name of Site
     * @param longitude - longitude of Site
     * @param latitude - latitude of Site
     */
    public Site(long code, String nom, float longitude, float latitude) {
        this.code = code;
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Getter of Code
     * @return the code of the site
     */
    public long getCode() {
        return this.code;
    }

    /**
     * Setter of Code
     * @param code the code of the site to set
     */
    public void setCode(long code) {
        this.code = code;
    }



    /**
     * Getter of Nom
     * @return the name of the site
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Setter of Nom
     * @param nom the name of the site to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }




    /**
     * Getter of Longitude
     * @return the longitude of the site
     */
    public float getLongitude() {
        return this.longitude;
    }

    
    /**
     * Setter of Longitude
     * @param longitude the longitude of the site to set
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }




    /**
     * Getter of Latitude
     * @return the latitude of the site
     */
    public float getLatitude() {
        return this.latitude;
    }

    /**
     * Setter of Latitude
     * @param latitude the latitude of the site to set
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
