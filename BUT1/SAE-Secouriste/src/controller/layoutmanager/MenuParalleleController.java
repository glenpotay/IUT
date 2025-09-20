package controller.layoutmanager;

import controller.common.DisplayUpComingEventController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import java.io.IOException;

import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * MenuParalleleController is responsible for managing the parallel menu in the application.
 * It handles the display of user profile, calendar, upcoming events, and settings.
 * This controller is used in both admin and common contexts.
 *
 * Authors: C.Brocart, T.Brami-Coatual, L.Carré, G.Potay
 * Version: 1.0
 */
public class MenuParalleleController {

    /**
     * The FenetreGestionController that manages the overall application window.
     * This controller is used to load different content into the main application window.
     */
    private FenetreGestionController fenetreGestionController;

    /**
     * The EvenementController that manages event-related functionalities.
     * This controller is used to handle event creation, modification, and display.
     */
    private DisplayUpComingEventController evenementController;

    /**
     * Flag to indicate whether the settings menu is currently open.
     * This is used to toggle the settings view on and off.
     */
    private boolean isOpenSettings = false;

    /**
     * The GridPane that serves as the layout for the parallel menu components.
     * This grid contains various UI elements such as profile display, calendar, and upcoming events.
     */
    @FXML
    private GridPane composentGrid;

    /**
     * Initializes the MenuParalleleController by setting up the initial view.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("MenuParalleleController initialized");
        setRow(0, "/fxml/common/DisplayProfil.fxml");
        setRow(1, "/fxml/common/DisplayCalendar.fxml");
        setRow(2, "/fxml/common/DisplayUpComingEvent.fxml");
    }

    /**
     * Links to the user profile page when the "Profile" button is clicked.
     * This method loads the user profile view into the main application window.
     */
    @FXML
    public void linkToSettings() {
        if (!isOpenSettings) {
            isOpenSettings = true;
            if (getInstanceAuthentificationManagement().isAdmin()) {

                fenetreGestionController.loadContent2("/fxml/common/DashboardSettings.fxml");
                setRow(0, "/fxml/common/DisplayProfil.fxml");
                setRow(1, "/fxml/common/DisplayCalendar.fxml");
                setRow(2, "/fxml/common/DisplayUpComingEvent.fxml");
            } else {

                fenetreGestionController.loadContent2("/fxml/common/DashboardSettings.fxml");
                setRow(1, "/fxml/common/DisplayCalendarDisponibilites.fxml");
                setRow(2, null);
            }
        } else {
            isOpenSettings = false;

            if (fenetreGestionController != null) {
                if (getInstanceAuthentificationManagement().isAdmin()) {
                    fenetreGestionController.loadContent2("/fxml/admin/DashboardEvent.fxml");
                } else {
                    fenetreGestionController.loadContent2("/fxml/common/DashboardCalendarAssignment.fxml");
                }
            }
            setRow(1, "/fxml/common/DisplayCalendar.fxml");
            setRow(2, "/fxml/common/DisplayUpComingEvent.fxml");
        }
    }

    /**
     * Sets the FenetreGestionController for this MenuParalleleController.
     * This method is used to inject the main application controller into this controller.
     *
     * @param fenetreGestionController The FenetreGestionController to set.
     */
    public void setFenetreGestionController(FenetreGestionController fenetreGestionController) {
        this.fenetreGestionController = fenetreGestionController;
    }

    /**
     * Sets a specific row in the GridPane with the given FXML path.
     * This method loads the FXML file and adds it to the specified row index in the GridPane.
     *
     * @param rowIndex The index of the row to set.
     * @param fxmlPath The path to the FXML file to load for this row.
     */
    public void setRow(int rowIndex, String fxmlPath) {
        try {
            composentGrid.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == rowIndex
            );

            if(fxmlPath != null) {



                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Node node = loader.load();

                Object controller = loader.getController();
                if (controller instanceof MenuParalleleInjectable injectable) {
                    injectable.setMenuParalleleController(this);
                }

                if (controller instanceof DisplayUpComingEventController) {
                    this.evenementController = (DisplayUpComingEventController) controller;
                }

                composentGrid.add(node, 0, rowIndex);
                GridPane.setHalignment(node, javafx.geometry.HPos.CENTER);
                GridPane.setValignment(node, javafx.geometry.VPos.BOTTOM);
                GridPane.setHgrow(node, javafx.scene.layout.Priority.ALWAYS);
                GridPane.setVgrow(node, javafx.scene.layout.Priority.ALWAYS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the FenetreGestionController associated with this MenuParalleleController.
     * This method is used to retrieve the main application controller.
     *
     * @return The FenetreGestionController instance.
     */
    public DisplayUpComingEventController getEvenementController() {
        return evenementController;
    }

    /**
     * Checks if the settings menu is currently open.
     * This method returns the status of the settings menu toggle.
     *
     * @return true if the settings menu is open, false otherwise.
     */
    public boolean isOpenSettings() {
        return isOpenSettings;
    }
}
