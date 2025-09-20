package controller.admin;

import controller.common.DisplayUpComingEventController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import model.data.persistence.Affectation;
import model.data.persistence.DPS;
import model.data.service.AffectationManagement;
import model.data.service.BesoinManagement;
import model.data.service.DPSManagement;

import static controller.layoutmanager.FenetreGestionController.removeOverlay;
import static controller.layoutmanager.FenetreGestionController.showOverlay;

/**
 * Controller for the DPS deletion confirmation window.
 * This controller handles the deletion of a DPS and its associated data.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class PopupDeleteDpsController {

    /**
     * The root pane of the window, used to manage the overlay.
     */
    @FXML
    private AnchorPane rootPane;

    /**
     * The stage of the window, used to close it after deletion.
     */
    private final DPSManagement dpsManagement = new DPSManagement();

    /**
     * The management service for affectations, used to handle related data.
     */
    private final AffectationManagement affectationManagement = new AffectationManagement();

    /**
     * The DPS to be deleted, initialized when the window is opened.
     */
    private DPS dps;

    /**
     * The controller for managing events, used to update the event list after deletion.
     */
    private DashboardEventController dashboardEventController;

    /**
     * The controller for managing events, used to update the event view after deletion.
     */
    private DisplayUpComingEventController evenementController;

    /**
     * The management service for needs, used to handle related data.
     */
    private BesoinManagement besoinManagement = new BesoinManagement();

    /**
     * Initializes the controller with the necessary data.
     *
     * @param dps The DPS to be deleted.
     * @param dashboardEventController The controller for managing events.
     * @param evenementController The controller for managing the event view.
     */
    public void initializeDPS(DPS dps, DashboardEventController dashboardEventController, DisplayUpComingEventController evenementController) {

        this.dps = dps;
        this.dashboardEventController = dashboardEventController;
        this.evenementController = evenementController;
        showOverlay();
    }

    /**
     * Deletes the DPS and all associated data.
     */
    @FXML
    public void supprDps() {
        this.besoinManagement.removeByDps(this.dps);
        for (Affectation affectation : this.affectationManagement.getAffectationsByDps(this.dps)) {
            this.affectationManagement.removeAffectation(affectation);
        }
        this.dpsManagement.removeDps(this.dps);
        this.dashboardEventController.retirerDpsList(dps);
        this.dashboardEventController.filtreUpdate();
        this.evenementController.initialize();
        annuleDPS();
    }

    /**
     * Cancels the deletion operation and closes the window.
     */
    @FXML
    public void annuleDPS() {
        removeOverlay();
        rootPane.getChildren().removeAll();
    }
}
