package model.data.persistence;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Secouriste class
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class Administrateur {
    /**
     * Informations personnelles du secouriste.
     */
    private long id;       // Identifiant unique
    private String nom;     // Nom du secouriste
    private String prenom;  // Prénom du secouriste
    private String dateNaissance; // Date de naissance (corrigé)
    private String tel;     // Numéro de téléphone
    private String adresse; // Adresse postale
    private byte[] photo;

    /**
     * Constructor of Secouriste
     *
     * @param id the id of the secouriste
     * @param nom the name of the secouriste
     * @param prenom the first name of the secouriste
     * @param dateNaissance the birth date of the secouriste
     * @param tel the phone number of the secouriste
     * @param adresse the address of the secouriste
     */
    public Administrateur(long id, String nom, String prenom, String dateNaissance, String tel, String adresse, byte[] photo) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.tel = tel;
        this.adresse = adresse;
        if (photo == null) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("images/anonyme.png")) {
                if (is == null) {
                    System.out.println("Image par défaut introuvable !");
                    this.photo = null;
                } else {
                    this.photo = is.readAllBytes();
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.photo = null;
            }
        } else {
            this.photo = photo;
        }
    }

    /**
     * Getter of IdSecouriste
     * 
     * @return the id of the secouriste
     */
    public long getIdAdministrateur() {
        return this.id;
    }

    /**
     * Getter of Nom
     * 
     * @return the name of the secouriste
     */
    public String getNom(){
        return this.nom;
    }


    /**
     * Getter of Prenom
     * 
     * @return the first name of the secouriste
     */
    public String getPrenom(){
        return this.prenom;
    }


    /**
     * Getter of DateNaissace
     * 
     * @return the birth date of the secouriste
     */
    public String getDateNaissance(){
        return this.dateNaissance;
    }

    /**
     * Setter of DateNaissace
     * @param dateNaissance the birth date of the secouriste
     */
    public void setDateNaissace(String dateNaissance){
        this.dateNaissance = dateNaissance;
    }

    /**
     * Getter of Tel
     * 
     * @return the phone number of the secouriste
     */
    public String getTel(){
        return this.tel;
    }

    /**
     * Setter of Tel
     * 
     * @param tel the phone number of the secouriste to set
     */
    public void setTel(String tel){
        this.tel = tel;
    }



    /**
     * Getter of Adresse
     * 
     * @return the address of the secouriste
     */
    public String getAdresse(){
        return this.adresse;
    }

    /**
     * Setter of Adresse
     * 
     * @param adresse the address of the secouriste to set
     */
    public void setAdresse(String adresse){
        this.adresse = adresse;
    }

    /**
     * Getter of Photo
     *
     * @return the photo of the secouriste
     */
    public byte[] getPhoto() {
        return photo;
    }

    /**
     * Setter of Photo
     *
     * @param photo the photo of the secouriste to set
     */
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    /**
     * Returns the photo as an Image object.
     *
     * @return the photo as an Image
     */
    public Image getPhotoAsImage() {
        return new Image(new ByteArrayInputStream(photo));
    }
}
