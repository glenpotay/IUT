package controller.auth;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import static controller.UtilsController.linkToPage;

/**
 * LoadingScreenController is responsible for handling the loading screen of the application.
 * It displays a loading screen for a few seconds before transitioning to the login page.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class LoadingScreenController {

    /**
     * The AnchorPane that serves as the loading page.
     */
    @FXML
    private AnchorPane pageChargement;

    /**
     * Initializes the LoadingScreenController by setting up a pause transition.
     * After 3 seconds, it links to the login page.
     */
    @FXML
    public void initialize() {

        System.out.println("LoadingScreenController initialized");
        PauseTransition pause = new PauseTransition(Duration.seconds((Math.random() * 3) + 2));
        pause.setOnFinished(event -> linkToPage(pageChargement, "/fxml/auth/Login.fxml"));
        pause.play();
    }
}
