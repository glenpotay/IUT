package controller.notif;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.data.persistence.Notification;
import model.data.service.AdministrateurManagement;
import model.data.service.DPSManagement;

import static controller.layoutmanager.FenetreGestionController.removeOverlay;
import static controller.layoutmanager.FenetreGestionController.showOverlay;

/**
 * Controller for the popup notification reader.
 * This class handles the display of a notification's details.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class PopupNotificationReaderController {

    /**
     * Label to display the title of the notification.
     * This label is updated with the notification's title when set.
     */
    @FXML
    private Label titleLabel;

    /**
     * Label to display the title of the notification.
     * This label is updated with the notification's title when set.
     */
    @FXML
    private Label fromTextlabel;

    /**
     * Label to display the sender of the notification.
     * This label is updated with the sender's name when set.
     */
    @FXML
    private Label toTextlabel;

    /**
     * Text area to display the message of the notification.
     * This area is populated with the notification's message when set.
     */
    @FXML
    private TextArea messageTextArea;

    /**
     * Label to display the date of the notification.
     * This label is updated with the notification's date when set.
     */
    @FXML
    private Label dateTextlabel;

    /**
     * Button to close the notification reader form.
     * This button is linked to the closeNotificationForm method.
     */
    @FXML
    private Button closeButtonNotificationForm;

    /**
     * The notification to be displayed in the reader.
     * This field is set when the notification is loaded.
     */
    private Notification notif;

    /**
     * Instance of AdministrateurManagement to manage administrator-related data.
     */
    private AdministrateurManagement admin = new AdministrateurManagement();

    /**
     * Instance of DPSManagement to manage DPS-related data.
     */
    private DPSManagement dps = new DPSManagement();

    /**
     * Initializes the controller after the FXML elements have been loaded.
     * This method is called automatically by the JavaFX framework.
     */
    @FXML
    private void initialize() {
        showOverlay();
    }

    /**
     * Closes the notification reader form and removes the overlay.
     * This method is called when the close button is clicked.
     */
    @FXML
    private void closeNotificationForm() {
        Stage stage = (Stage) closeButtonNotificationForm.getScene().getWindow();
        stage.close();
        removeOverlay();
    }

    /**
     * Sets the notification to be displayed in the reader.
     * This method updates the UI elements with the notification's details.
     *
     * @param notif The notification to display.
     */
    public void setNotification(Notification notif) {
        this.notif = notif;
        titleLabel.setText(notif.getTitle());
        fromTextlabel.setText(admin.getAdministrateurById(notif.getSender()).getNom());
        toTextlabel.setText(dps.getDpsById(notif.getIdDPS()).getName());
        dateTextlabel.setText(notif.getDate());
        messageTextArea.setText(notif.getMessage());
    }
}

