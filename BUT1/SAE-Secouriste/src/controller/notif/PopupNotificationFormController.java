package controller.notif;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.data.service.AuthentificationManagement;
import model.data.service.DPSManagement;
import model.data.service.NotificationManagement;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static controller.layoutmanager.FenetreGestionController.removeOverlay;
import static controller.layoutmanager.FenetreGestionController.showOverlay;
import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * Controller for the popup notification form.
 * This class handles the creation of new notifications.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class PopupNotificationFormController {

    /**
     * Current date and time, used for the notification timestamp.
     */
    private LocalDateTime now = LocalDateTime.now();

    /**
     * Instance of AuthentificationManagement to handle user authentication.
     */
    private final AuthentificationManagement auth = getInstanceAuthentificationManagement();;

    /**
     * Instance of DPSManagement to manage DPS-related data.
     */
    private final DPSManagement dpsMana = new DPSManagement();

    /**
     * Instance of NotificationManagement to handle notification operations.
     */
    private final NotificationManagement notificationManagement = new NotificationManagement();

    private DisplayNotificationController displayNotificationController;

    /**
     * FXML elements for the notification form.
     */
    @FXML
    private TextField subjectTextField;

    /**
     * Label to display the current date and time.
     */
    @FXML
    private Label dateTextlabel;

    /**
     * Label to display the sender's name.
     */
    @FXML
    private Label fromTextlabel;

    /**
     * Text area for the notification message.
     */
    @FXML
    private TextArea messageTextArea;

    /**
     * ComboBox to select the DPS to which the notification will be sent.
     */
    @FXML
    private ComboBox<String> dpsComboBox;

    /**
     * AnchorPane for the notification form layout.
     */
    @FXML
    private AnchorPane notificationFormAnchorPane;

    /**
     * Button to close the notification form.
     */
    @FXML
    private Button closeButtonNotificationForm;

    /**
     * Initializes the notification form by setting up the date, sender, and DPS options.
     */
    @FXML
    public void initialize() {
        showOverlay(); // Show the overlay when the form is initialized ( background dimming )
        try {
            // Chemin corrigé pour le fichier CSS
            String cssPath = "/css/error.css"; // Changé de errors.css à error.css
            if (getClass().getResource(cssPath) != null) {
                notificationFormAnchorPane.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            } else {
                System.err.println("Fichier CSS non trouvé: " + cssPath);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du CSS: " + e.getMessage());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'h'mm d MMMM", Locale.FRENCH); // Define the date format for the notification timestamp
        String formatted = now.format(formatter);
        dateTextlabel.setText(formatted);

        String UserName = auth.getCurrentUserName(); // Get the current user's name from the authentication management
        System.out.println("UserName: " + UserName);
        fromTextlabel.setText(UserName);

        dpsComboBox.setItems(FXCollections.observableArrayList(dpsMana.getDpsName())); // Populate the ComboBox with DPS names from the DPS management service
    }

    /**
     * Create a Box Massage to use in the eventController and
     */
    public void sendMessage() {
        boolean valid = true;

        // Subject
        if (subjectTextField.getText() == null || subjectTextField.getText().trim().isEmpty()) {
            if (!subjectTextField.getStyleClass().contains("field-error")) {
                subjectTextField.getStyleClass().add("field-error");
            }
            valid = false;
        } else {
            subjectTextField.getStyleClass().removeAll("field-error");
        }

        // Message
        if (messageTextArea.getText() == null || messageTextArea.getText().trim().isEmpty()) {
            if (!messageTextArea.getStyleClass().contains("field-error")) {
                messageTextArea.getStyleClass().add("field-error");
            }
            valid = false;
        } else {
            messageTextArea.getStyleClass().removeAll("field-error");
        }

        // ComboBox
        if (dpsComboBox.getValue() == null || dpsComboBox.getValue().trim().isEmpty()) {
            if (!dpsComboBox.getStyleClass().contains("comboBox-error")) {
                dpsComboBox.getStyleClass().add("comboBox-error");
            }
            valid = false;
        } else {
            dpsComboBox.getStyleClass().removeAll("comboBox-error");
        }

        if (!valid) return;

        String title = subjectTextField.getText();
        messageTextArea.setWrapText(true);
        String message = messageTextArea.getText();
        String date = dateTextlabel.getText();
        String DPSName = dpsComboBox.getValue();

        notificationManagement.createNotification(title, message, date, DPSName);


        closeNotificationForm();

    }


    /**
     * Closes the notification form and removes the overlay.
     */
    @FXML
    private void closeNotificationForm() {
        removeOverlay();
        Stage stage = (Stage) closeButtonNotificationForm.getScene().getWindow();
        stage.close();
        displayNotificationController.initialize();
    }


    public void setDisplayNotificationController(DisplayNotificationController displayNotificationController) {
        this.displayNotificationController = displayNotificationController;
    }
}
