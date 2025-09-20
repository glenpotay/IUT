package controller.layoutmanager;

import controller.admin.DashboardEventController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * Controller class for managing the main administration window.
 * Handles loading and switching of embedded views (menus, event management, calendar assignment).
 *
 * @author C.Brocart, T.Brami-Coatual, L.Carré, G.Potay
 * @version 1.0
 */
public class FenetreGestionController {

    /** Pane for including the parallel menu content */
    @FXML
    private StackPane includePane1;

    /** Pane for including the main content (event management, calendar, etc.) */
    @FXML
    private StackPane includePane2;

    /** The root pane of the management window */
    @FXML
    private AnchorPane fenetreGestion;

    /** Static reference to the root pane for overlay management */
    private static AnchorPane staticFenetreGestion;

    /** Controller for the parallel menu view */
    private MenuParalleleController menuParalleleController;

    /**
     * Initializes the controller by loading the menu and appropriate main content
     * based on the user's admin status.
     */
    @FXML
    public void initialize() {
        staticFenetreGestion = fenetreGestion;
        loadContent1("/fxml/layoutmanager/MenuParallele.fxml");
        if (getInstanceAuthentificationManagement().isAdmin()) {
            loadContent2("/fxml/admin/DashboardEvent.fxml");
        } else {
            loadContent2("/fxml/common/DashboardCalendarAssignment.fxml");
        }
    }

    /**
     * Loads and sets the content of the first include pane with the specified FXML file.
     * Also sets the controller for the loaded content, if applicable.
     *
     * @param fxmlFile - the path to the FXML file to load into includePane1
     */
    public void loadContent1(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newContent = loader.load();

            this.includePane1.getChildren().setAll(newContent);
            Object controller = loader.getController();

            this.menuParalleleController = (MenuParalleleController) controller;
            ((MenuParalleleController)controller).setFenetreGestionController(this);

        } catch (IOException e) {
            controller.UtilsController.showError("Erreur lors du chargement de " + fxmlFile + " : " + e.getMessage());
        }
    }

    /**
     * Loads and sets the content of the second include pane with the specified FXML file.
     * Passes a reference to this controller to the loaded content's controller if supported.
     *
     * @param fxmlFile - the path to the FXML file to load into includePane2
     */
    public void loadContent2(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newContent = loader.load();
            this.includePane2.getChildren().setAll(newContent);

            Object controller = loader.getController();

            if (controller instanceof FenetreGestionInjectable fenetreGestioninjectable) {
                fenetreGestioninjectable.setFenetreGestionController(this);
            }

            if (controller instanceof MenuParalleleInjectable menuParalleleInjectable) {
                menuParalleleInjectable.setMenuParalleleController(this.menuParalleleController);
            }

            if (controller instanceof DashboardEventController dashboardEventController) {
                dashboardEventController.setEvenementController(this.menuParalleleController.getEvenementController());
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
            controller.UtilsController.showError("Erreur lors du chargement de " + fxmlFile + " : " + e.getMessage());
        }
    }

    /**
     * Returns the root AnchorPane of the management window.
     *
     * @return the root AnchorPane
     */
    public AnchorPane getFenetreGestion() {
        return this.fenetreGestion;
    }

    /**
     * Creates and shows a semi-transparent overlay pane on top of the management window.
     * Useful for modal dialogs or disabling interaction with underlying UI.
     *
     * @return the overlay StackPane that was added
     */
    public static StackPane showOverlay() {
        StackPane overlayPane = new StackPane();
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlayPane.setId("fenetreGestionOverlay");
        AnchorPane.setTopAnchor(overlayPane, 0.0);
        AnchorPane.setBottomAnchor(overlayPane, 0.0);
        AnchorPane.setLeftAnchor(overlayPane, 0.0);
        AnchorPane.setRightAnchor(overlayPane, 0.0);
        staticFenetreGestion.getChildren().add(overlayPane);
        return overlayPane;
    }

    /**
     * Removes the previously shown overlay pane from the management window.
     */
    public static void removeOverlay() {
        staticFenetreGestion.getChildren().removeIf(node ->
            node instanceof StackPane && "fenetreGestionOverlay".equals(node.getId())
        );
    }
}
