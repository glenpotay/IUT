package controller.auth;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;

import static controller.UtilsController.linkToPage;
import static controller.UtilsController.showError;
import static controller.UtilsController.showInfo;
import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * MotDePasseOublieController is responsible for handling the page where users can change their forgotten password.
 * It provides functionality to validate the code sent to the user's email and change the password accordingly.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class ForgottenPasswordController {
    /**
     * The AnchorPane that serves as the page for changing the forgotten password.
     */
    @FXML
    private AnchorPane pageForgotPassword;
    /**
     * The TextField for entering the code sent to the user's email.
     */
    @FXML
    private TextField codeTextField;
    /**
     * The TextField for entering the new password.
     */
    @FXML
    private TextField passwordTextField;

    /**
     * Initializes the MotDePasseOublieController by setting up the page.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    private void linkToConnexion() {
        linkToPage(pageForgotPassword, "/fxml/auth/Login.fxml");
    }

    /**
     * Handles the button click event to validate the change of password.
     * It checks if the code is valid and changes the password accordingly.
     * If successful, it shows a success message and navigates to the login page;
     * otherwise, it shows an error message.
     */
    @FXML
    private void validateChange() {
        try {
            long code = Long.parseLong(codeTextField.getText());
            if (getInstanceAuthentificationManagement().changePassword(code, passwordTextField.getText())) {
                showInfo("Le mot de passe a été changé avec succès !");
                linkToConnexion();
            } else {
                showError("Le mot de passe n'a pas été changé, le code n'est pas bon !");
            }
        } catch (NumberFormatException e) {
            showError("Le code doit être un nombre.");
        }
    }
}