package controller.common;

import controller.layoutmanager.MenuParalleleController;
import controller.layoutmanager.MenuParalleleInjectable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import model.data.persistence.Administrateur;
import model.data.persistence.Secouriste;
import model.data.service.AdministrateurManagement;
import model.data.service.AuthentificationManagement;
import model.data.service.SecouristeManagement;

import java.io.ByteArrayInputStream;

import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * DisplayProfilController is responsible for managing the user profile view.
 * It displays the user's name, role, and profile picture, and allows toggling between notifications and calendar views.
 * It implements MenuParalleleInjectable to interact with the parallel menu controller.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DisplayProfilController implements MenuParalleleInjectable {

    /**
     * The Circle that displays the user's profile picture.
     */
    @FXML
    Circle myCircle;

    /**
     * The Label that displays the user's name.
     */
    @FXML
    Label nomLabel;

    /**
     * The Label that indicates whether the user is an administrator or a rescuer.
     */
    @FXML
    Label adminSecourLabel;

    /**
     * The Button that toggles between notifications and calendar views.
     */
    @FXML
    Button notifBouton;

    /**
     * The Button that allows the user to switch to the calendar view.
     */
    private AdministrateurManagement administrateurManagement = new AdministrateurManagement();

    /**
     * The SecouristeManagement that handles rescuer-related operations.
     */
    private SecouristeManagement secouristeManagement = new SecouristeManagement();

    /**
     * The MenuParalleleController that manages the parallel menu in the application.
     * This controller is used to switch between different views in the parallel menu.
     */
    private MenuParalleleController menuParalleleController;

    /**
     * Flag to indicate whether the notifications view is currently displayed.
     * This is used to toggle between notifications and calendar views.
     */
    private boolean isNotif = false ;

    /**
     * Initializes the DisplayProfilController by setting up the user profile view.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        AuthentificationManagement auth = getInstanceAuthentificationManagement();
        if (auth.getCurrentUser().getRole().equals("rescuer")) {
            Secouriste secouriste = secouristeManagement.getSecouristeById(auth.getCurrentUser().getIdUser());
            nomLabel.setText(secouriste.getPrenom() + " " + secouriste.getNom());
            adminSecourLabel.setText("Secouriste");
        } else {
            Administrateur administrateur = administrateurManagement.getAdministrateurById(auth.getCurrentUser().getIdUser());
            nomLabel.setText(administrateur.getPrenom() + " " + administrateur.getNom());
            adminSecourLabel.setText("Administrateur");
        }

        myCircle.setStroke(Color.BLACK);
        Secouriste secouriste;
        Administrateur administrateur;
        Image image = null;
        if (getInstanceAuthentificationManagement().isAdmin()) {
            administrateur = administrateurManagement.getAdministrateurById(getInstanceAuthentificationManagement().getCurrentUser().getIdUser());
            if (administrateur.getPhoto() != null) {
                image = new Image(new ByteArrayInputStream(administrateur.getPhoto()));
                if (!image.isError()) {
                    myCircle.setFill(new ImagePattern(image));
                }
            }
        } else {
            secouriste = secouristeManagement.getSecouristeById(getInstanceAuthentificationManagement().getCurrentUser().getIdUser());
            if (secouriste.getPhoto() != null) {
                image = new Image(new ByteArrayInputStream(secouriste.getPhoto()));
                if (!image.isError()) {
                    myCircle.setFill(new ImagePattern(image));
                }
            }
        }

        if (image != null) {
            myCircle.setFill(new ImagePattern(image));
        }
    }

    /**
     * Switches between the notifications view and the calendar view.
     */
    @FXML
    private void switchNotifCalendar() {
        if (menuParalleleController != null) {
            if (!isNotif) {
                menuParalleleController.setRow(1, "/fxml/notif/DisplayNotification.fxml");
                isNotif = true ;
            } else {
                if(menuParalleleController.isOpenSettings() && !getInstanceAuthentificationManagement().isAdmin()) {
                    menuParalleleController.setRow(1, "/fxml/common/DisplayCalendarDisponibilites.fxml");
                } else {
                    menuParalleleController.setRow(1, "/fxml/common/DisplayCalendar.fxml");
                }
                isNotif = false ;
            }
        }
    }

    /**
     * Sets the MenuParalleleController for this DisplayProfilController.
     * This method is used to inject the parallel menu controller into this controller.
     *
     * @param controller The MenuParalleleController to set.
     */
    public void setMenuParalleleController(MenuParalleleController controller) {
        this.menuParalleleController = controller;
    }
}
