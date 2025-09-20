package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

/**
 * UtilsController provides utility methods for displaying alerts and linking to different pages in the application.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class UtilsController {

    /**
     * Shows an error alert with the specified message.
     *
     * @param message the message to display in the alert
     */
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information alert with the specified message.
     *
     * @param message the message to display in the alert
     */
    public static void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Links to a specified FXML page and replaces the content of the given AnchorPane.
     *
     * @param parent   the AnchorPane to replace content in
     * @param fxmlPath the path to the FXML file to load
     */
    public static void linkToPage(AnchorPane parent, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilsController.class.getResource(fxmlPath));
            AnchorPane page = loader.load();
            parent.getChildren().setAll(page);
            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

        } catch (IOException e) {
            showError("Erreur lors du chargement de la page : " + fxmlPath);
        }
    }
}