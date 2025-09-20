package controller.auth;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import static controller.UtilsController.*;
import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * RegistrationFormController is responsible for handling the registration form page.
 * It provides functionality to validate user input and create a rescuer profile.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class RegistrationFormController {

    /**
     * The AnchorPane that serves as the registration form page.
     */
    @FXML
    private AnchorPane pageRegistrationForm;

    /**
     * The TextField for entering the user's last name.
     */
    @FXML
    private TextField nameTextField;

    /**
     * The TextField for entering the user's first name.
     */
    @FXML
    private TextField forenameTextField;

    /**
     * The TextField for entering the user's birthdate in the format JJ/MM/AAAA.
     */
    @FXML
    private TextField birthdateTextField;

    /**
     * The TextField for entering the user's address.
     */
    @FXML
    private TextField addressTextField;

    /**
     * The TextField for entering the user's phone number.
     * It should contain exactly 10 digits.
     */
    @FXML
    private TextField phoneNumberTextField;

    /**
     * This method is called when the button to register is clicked.
     * it validates the input fields and creates a rescuer profile if all fields are valid.
     */
    @FXML
    private void buttonVerifyClicked() {
        // Initialisation of the validation flag
        boolean isValid = true;

        // Validate the name field
        String name = nameTextField.getText().trim();
        if (name.isEmpty() || !name.matches("[a-zA-ZÀ-ÿ\\s-]+")) { // Regex to allow letters, accented characters, spaces, and hyphens but not numbers or special characters
            nameTextField.setStyle("-fx-prompt-text-fill: #FF004D;"); // Set the prompt text color to red
            nameTextField.setText(""); // Clear the text field
            nameTextField.setPromptText("Nom invalide"); // Set the prompt text to indicate the error
            isValid = false;
        } else {
            nameTextField.setStyle("");
        }

        // Validate the forename field
        String forename = forenameTextField.getText().trim();
        if (forename.isEmpty() || !forename.matches("[a-zA-ZÀ-ÿ\\s-]+")) {
            forenameTextField.setStyle("-fx-prompt-text-fill: #FF004D;");
            forenameTextField.setText("");
            forenameTextField.setPromptText("Prénom invalide");
            isValid = false;
        } else {
            forenameTextField.setStyle("");
        }

        // Validate the birthdate field
        String birthdate = birthdateTextField.getText().trim();
        if (!birthdate.matches("\\d{2}/\\d{2}/\\d{4}")) {
            birthdateTextField.setStyle("-fx-prompt-text-fill: #FF004D;");
            birthdateTextField.setText("");
            birthdateTextField.setPromptText("Format JJ/MM/AAAA requis");
            isValid = false;
        } else {
            birthdateTextField.setStyle("");
        }

        // Validate the address field
        String address = addressTextField.getText().trim();
        if (address.isEmpty()) {
            addressTextField.setStyle("-fx-prompt-text-fill: #FF004D;");
            addressTextField.setText("");
            addressTextField.setPromptText("Adresse requise");
            isValid = false;
        } else {
            addressTextField.setStyle("");
        }

        // Validate the phone number field
        String phoneNumber = phoneNumberTextField.getText().trim();
        if (!phoneNumber.matches("\\d{10}")) {
            phoneNumberTextField.setStyle("-fx-prompt-text-fill: #FF004D;");
            phoneNumberTextField.setText("");
            phoneNumberTextField.setPromptText("10 chiffres requis");
            isValid = false;
        } else {
            phoneNumberTextField.setStyle("");
        }

        // If all fields are valid, create the rescuer profile
        if (isValid) {
            if (getInstanceAuthentificationManagement().createRescuer(getInstanceAuthentificationManagement().getCurrentUser().getIdUser(), name, forename, birthdate, phoneNumber, address,  null)) {
                // Show success message
                showInfo("Informations du secouriste enregistrées avec succès !");
                linkToPage(pageRegistrationForm, "/fxml/auth/Login.fxml");
            } else {
                showError("Impossible de créer le secouriste. Veuillez vérifier vos informations.");
            }
        }
    }
}