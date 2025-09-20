package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import model.data.persistence.*;
import model.data.service.*;

import static controller.layoutmanager.FenetreGestionController.removeOverlay;

/**
 * Controller for the Set Admin Rescuer window.
 * This controller handles the logic for promoting a rescuer to an administrator.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class PopupRescuerSetAdminController {

    /**
     * The root pane of the FXML layout, used for managing the UI components.
     */
    @FXML
    private AnchorPane rootPane;

    /**
     * Management classes for various entities in the application.
     */
    private final SecouristeManagement secouristeManagement = new SecouristeManagement();

    /**
     * Management class for handling rescuer assignments.
     */
    private final AffectationManagement affectationManagement = new AffectationManagement();

    /**
     * Management class for handling rescuer competencies.
     */
    private final PossessionManagement possessionManagement = new PossessionManagement();

    /**
     * Management class for handling rescuer availability.
     */
    private final DisponibiliteManagement disponibiliteManagement = new DisponibiliteManagement();

    /**
     * Management class for handling administrator operations.
     */
    private final AdministrateurManagement administrateurManagement = new AdministrateurManagement();

    /**
     * Management class for handling authentication operations.
     */
    private final AuthentificationManagement authentificationManagement = AuthentificationManagement.getInstanceAuthentificationManagement();;

    /**
     * The rescuer being promoted to administrator.
     */
    private Secouriste secouriste;

    /**
     * Controller for managing rescuers, used to update the rescuer list after promotion.
     */
    private DashboardRescuerController dashboardRescuerController;

    /**
     * Initializes the controller with the rescuer to be promoted and the rescuer management controller.
     *
     * @param secouriste The rescuer to be promoted to administrator.
     * @param dashboardRescuerController The controller for managing rescuers.
     */
    public void initializeSetAdminSecouriste(Secouriste secouriste, DashboardRescuerController dashboardRescuerController) {
        this.secouriste = secouriste;
        this.dashboardRescuerController = dashboardRescuerController;
    }

    /**
     * Sets the rescuer as an administrator and updates the necessary data.
     * This method removes the rescuer's assignments, availability, and competencies,
     * promotes them to administrator, and updates the rescuer list in the management controller.
     */
    @FXML
    public void setAdmin() {
        Administrateur newAdmin = new Administrateur(this.secouriste.getIdSecouriste(), this.secouriste.getNom(), this.secouriste.getPrenom(), this.secouriste.getDateNaissance(), this.secouriste.getTel(), this.secouriste.getAdresse(), this.secouriste.getPhoto());
        this.administrateurManagement.addAdministrateur(newAdmin);

        for (Affectation affectation : this.affectationManagement.getAffectationsByRescuer(this.secouriste)) {
            this.affectationManagement.removeAffectation(affectation);
        }

        for (Disponibilite disponibilite : this.disponibiliteManagement.getDisponibilites(this.secouriste)) {
            this.disponibiliteManagement.removeDisponibilite(this.secouriste, disponibilite.getJourDisp());
        }

        for (Competence competence : this.possessionManagement.getPossessionBySecouriste(this.secouriste).getCompetencesSec()) {
            this.possessionManagement.removePossession(secouriste, competence);
        }

        this.authentificationManagement.changeRole(this.secouristeManagement.getUserBySecouriste(this.secouriste), "administrator");
        this.secouristeManagement.removeSecouriste(this.secouriste);
        this.dashboardRescuerController.retirerList(secouriste);
        this.dashboardRescuerController.filtreUpdate();
        annuleAdmin();
    }

    /**
     * Cancels the promotion of the rescuer to administrator and closes the window.
     * This method removes the overlay and clears the root pane.
     */
    @FXML
    public void annuleAdmin() {
        removeOverlay();
        rootPane.getChildren().removeAll();
    }
}
