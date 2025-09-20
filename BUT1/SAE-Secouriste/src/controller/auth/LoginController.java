package controller.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.data.service.AuthentificationManagement;
import static controller.UtilsController.linkToPage;
import static controller.UtilsController.showError;
import static model.data.service.AuthentificationManagement.LoginResult.*;
import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * ConnexionController is responsible for handling the login page of the application.
 * It provides functionality to log in, navigate to the registration page, and recover forgotten passwords.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class LoginController {

    /**
     * The AnchorPane that serves as the login page.
     */
    @FXML
    private TextField mailTextField;

    /**
     * The PasswordField for entering the password.
     */
    @FXML
    private PasswordField passwordPasswordField;

    /**
     * The AnchorPane that contains the login page layout.
     */
    @FXML
    private AnchorPane pageConnexion;

    /**
     * Initializes the ConnexionController by setting up the login page.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("ConnexionController initialized");
    }

    /**
     * Links to the registration page when the "Register" button is clicked.
     */
    @FXML
    public void linkToRegister() {
        linkToPage(pageConnexion, "/fxml/auth/Registration.fxml");
    }

    /**
     * Links to the password recovery page when the "Forgot Password" link is clicked.
     */
    @FXML
    public void linkToReceiveCode() {
        linkToPage(pageConnexion, "/fxml/auth/SendCode.fxml");
    }

    /**
     * This method is called when the "Quick Login" button is clicked.
     * It retrieves the last logged-in user's email and password from preferences
     */
    @FXML
    public void quickLogin() {
        // Takes the last email and password from the preferences
        java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(LoginController.class);
        String lastEmail = prefs.get("lastEmail", "");
        String lastPW = prefs.get("lastPW", "");

        // if the last email and password are not empty, fill the text fields
        if (!lastEmail.isEmpty() && (!lastPW.isEmpty())) {
            mailTextField.setText(lastEmail);
            passwordPasswordField.setText(lastPW);


        } else {
            // If no previous connection, set the prompt text to "Aucune connexion précédente"
            mailTextField.setPromptText("Aucune connexion précédente");
        }
    }


    /**
     * Handles the login button click event.
     * It attempts to log in with the provided email and password.
     * If successful, it navigates to the event page; otherwise, it shows an error message popUp created in the utilsController.
     */
    @FXML
    public void ButtonConnexionClicked() {
        try {
            // Verification of email and password fields with the methode login from AuthentificationManagement
            AuthentificationManagement.LoginResult result = getInstanceAuthentificationManagement().login(mailTextField.getText(), passwordPasswordField.getText());

            // Check the result of the login attempt
            if (result == SUCCESS) {
                // Save the email and password in preferences for quick login next time
                java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(LoginController.class);
                prefs.put("lastEmail", mailTextField.getText());
                prefs.put("lastPW", passwordPasswordField.getText());

                linkToPage(pageConnexion, "/fxml/layoutmanager/FenetreGestion.fxml");

            // This result indicate that the rescuer didn't complete the registration form yet after the creation of the account
            } else if (result == INVALID_RESCUER) {
                //
                java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(LoginController.class);
                prefs.put("lastEmail", mailTextField.getText());
                prefs.put("lastPW", passwordPasswordField.getText());

                // Navigate to the registration form for rescuers to take the necessary information
                linkToPage(pageConnexion, "/fxml/auth/RegistrationForm.fxml");
            } else {

                // Login failed, show error messages based on the result
                if (result == INVALID_LOGIN) {
                    mailTextField.clear();
                    mailTextField.setPromptText("Adresse mail inconnue");
                    mailTextField.requestFocus();

                    passwordPasswordField.clear();
                    passwordPasswordField.requestFocus();
                }
                if (result == INVALID_PASSWORD) {
                    passwordPasswordField.clear();
                    passwordPasswordField.setPromptText("Mot de passe incorrect");
                    passwordPasswordField.requestFocus();
                }
            }
        } catch (Exception e) {
            showError("Une erreur est survenue lors de la connexion. Veuillez réessayer plus tard.");
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
        }
    }

    /**
     * Handles the Enter key press event to trigger the login action.
     * If the Enter key is pressed, it calls the ButtonConnexionClicked method.
     *
     * @param keyEvent The KeyEvent triggered by the key press.
     */
    @FXML
    public void enterConnectionClicked(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            ButtonConnexionClicked();
        }
    }
}
