package controller.map;

import controller.layoutmanager.FenetreGestionController;
import controller.layoutmanager.FenetreGestionInjectable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;

import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * GestionMapController is responsible for managing the map view in the application.
 * It initializes the map and handles button events for navigating to different functionalities.
 * This controller is used in both admin and common contexts.
 *
 * Authors: C.Brocart, T.Brami-Coatual, L.Carré, G.Potay
 * Version: 1.0
 */
public class GestionMapController implements FenetreGestionInjectable {


    /**
     * The AnchorPane that serves as the map view.
     */
    @FXML
    private FlowPane carteFlowPane;

    /**
     * The AnchorPane that contains the map management interface.
     * This is used to display the map and related functionalities.
     */
    @FXML
    private AnchorPane gestionCarte;

    /**
     * The Button that allows the user to view or manage events.
     * This button is visible only to admin users.
     */
    @FXML
    private AnchorPane carteAnchorPane;

    @FXML
    private Button planningButton;

    /**
     * The Button that allows the user to manage the map or return to the planning view.
     * This button is visible to both admin and common users.
     */
    @FXML
    private Button gestionButton;

    /**
     * The FenetreGestionController that manages the overall application window.
     * This controller is used to load different content into the main application window.
     */
    private FenetreGestionController fenetreGestionController;

    /**
     * Initializes the GestionMapController by setting up the map view and buttons.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        try {
            if (getInstanceAuthentificationManagement().isAdmin()) {
                planningButton.setVisible(true);
                planningButton.disableProperty().set(false);
            } else {
                gestionButton.setText("Retour au planning");
            }


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/map/Map.fxml"));
            AnchorPane page = loader.load();
            carteAnchorPane.getChildren().setAll(page);

            Rectangle clip = new Rectangle();
            clip.setArcWidth(60);
            clip.setArcHeight(60);
            clip.widthProperty().bind(page.widthProperty());
            clip.heightProperty().bind(page.heightProperty());

            page.setClip(clip);
        } catch(Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Handles the button click event to navigate to the event management page.
     * If the user is an admin, it loads the event management page.
     * Otherwise, it navigates to the calendar assignment page.
     */
    @FXML
    public void evenementButton() {
        if (getInstanceAuthentificationManagement().isAdmin()) {
            fenetreGestionController.loadContent2("/fxml/admin/DashboardEvent.fxml");
        } else {
            calendarAssignment();
        }
    }

    /**
     * Handles the button click event to navigate to the map management page.
     * If the user is an admin, it loads the map management page.
     * Otherwise, it navigates to the calendar assignment page.
     */
    @FXML
    public void calendarAssignment() {
        fenetreGestionController.loadContent2("/fxml/common/DashboardCalendarAssignment.fxml");
    }

    /**
     * Sets the FenetreGestionController for this controller.
     * This method is used to inject the main application controller into this controller.
     *
     * @param fenetreGestionController The FenetreGestionController to set.
     */
    public void setFenetreGestionController(FenetreGestionController fenetreGestionController) {
        this.fenetreGestionController = fenetreGestionController;
    }
}
