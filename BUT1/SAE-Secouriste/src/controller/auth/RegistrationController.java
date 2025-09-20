package controller.auth;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;

import static controller.UtilsController.linkToPage;
import static controller.UtilsController.showError;
import static controller.UtilsController.showInfo;
import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * RegistrationController is responsible for handling the registration page of the application.
 * It provides functionality to register a new user by entering their email and password.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class RegistrationController {

    /**
     * The AnchorPane that serves as the registration page.
     */
    @FXML
    private TextField labelMail;

    /**
     * The PasswordField for entering the password.
     */
    @FXML
    private PasswordField labelPassword;

    /**
     * The PasswordField for confirming the new password.
     */
    @FXML
    private PasswordField newPasswordConfirmation;

    /**
     * The AnchorPane that contains the registration page layout.
     */
    @FXML
    private AnchorPane pageRegister;

    /**
     * Initializes the InscriptionController by setting up the registration page.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    public void ButtonRegisterClicked() {
        try {
            System.out.println("Inscription button clicked");
            System.out.println("Email: " + labelMail.getText());
            if (getInstanceAuthentificationManagement().register(labelMail.getText(), labelPassword.getText(), newPasswordConfirmation.getText())) {
                showInfo("L'inscription a réussi. Vous pouvez maintenant vous connecter.");
                linkToPage(pageRegister, "/fxml/auth/RegistrationForm.fxml");
            } else {
                showError("L'inscription a échoué. Veuillez vérifier vos informations.");
            }
        } catch (SQLException e) {
            showError("Une erreur est survenue lors de l'inscription. Veuillez réessayer plus tard.");
            System.out.println("Email: " + e.getMessage());
        }
    }

    /**
     * Links to the login page when the "connexion" link is clicked.
     */
    @FXML
    private void linkToConnexion() {
        linkToPage(pageRegister, "/fxml/auth/Login.fxml");
    }
}