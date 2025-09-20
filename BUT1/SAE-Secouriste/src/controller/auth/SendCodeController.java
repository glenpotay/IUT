package controller.auth;

import controller.UtilsController;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import static controller.UtilsController.linkToPage;
import static controller.UtilsController.showInfo;
import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * EnvoiCodeController is responsible for handling the page where users can request a code
 * to recover their password by entering their email address.
 * It provides functionality to send a code to the user's email and navigate to the password recovery page.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class SendCodeController {

    /**
     * The AnchorPane that serves as the page for sending the code.
     */
    @FXML
    private AnchorPane pageCode;

    /**
     * The TextField for entering the user's email address.
     */
    @FXML
    private TextField mailTextField;

    /**
     * Initializes the EnvoiCodeController by setting up the page.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    private void linkToConnexion() {
        linkToPage(pageCode, "/fxml/auth/Login.fxml");
    }

    /**
     * Handles the button click event to send a code to the user's email.
     * If the email exists, it sends a code and navigates to the password recovery page.
     * If the email does not exist, it shows an error message.
     */
    @FXML
    private void ButtonGetCode() {
        if (getInstanceAuthentificationManagement().ReceiveCode(mailTextField.getText())) {
            showInfo("Code envoyé avec succès !");
            linkToPage(pageCode, "/fxml/auth/ForgottenPassword.fxml");
        } else {
            UtilsController.showError("Mail inexistant");
        }
    }
}

