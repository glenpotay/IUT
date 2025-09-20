package controller.common;

import controller.UtilsController;
import controller.layoutmanager.FenetreGestionController;
import controller.layoutmanager.FenetreGestionInjectable;
import controller.layoutmanager.MenuParalleleController;
import controller.layoutmanager.MenuParalleleInjectable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.dao.AdministrateurDAO;
import model.dao.DisponibiliteDAO;
import model.dao.PossessionDAO;
import model.data.persistence.Administrateur;
import model.data.persistence.Competence;
import model.data.persistence.Possession;
import model.data.persistence.Secouriste;
import model.data.service.AdministrateurManagement;
import model.data.service.PossessionManagement;
import model.data.service.SecouristeManagement;

import java.io.*;
import java.util.*;

import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * DashboardSettingsController is responsible for managing the settings page of the application.
 * It allows users to update their profile picture, manage their competencies, and perform administrative actions.
 * This controller is used in both admin and common contexts.
 *
 * Authors: C.Brocart, T.Brami-Coatual, L.Carré, G.Potay
 * Version: 1.0
 */
public class DashboardSettingsController implements FenetreGestionInjectable, MenuParalleleInjectable {

    /**
     * The FenetreGestionController that manages the overall application window.
     * This controller is used to load different content into the main application window.
     */
    private FenetreGestionController fenetreGestionController;

    /**
     * The SecouristeManagement that handles rescuer-related operations.
     * This management class is used to interact with rescuer data.
     */
    private final SecouristeManagement secouristeManagement = new SecouristeManagement();

    /**
     * The AdministrateurManagement that handles administrator-related operations.
     * This management class is used to interact with administrator data.
     */
    private final AdministrateurManagement administrateurManagement = new AdministrateurManagement();

    /**
     * The AdministrateurDAO that provides data access methods for administrators.
     * This DAO is used to perform CRUD operations on administrator data.
     */
    private final AdministrateurDAO administrateurDAO = new AdministrateurDAO();

    /**
     * The Secouriste object representing the current rescuer.
     * This object is used to display and manage the rescuer's profile.
     */
    Secouriste sec;

    /**
     * The Administrateur object representing the current administrator.
     * This object is used to display and manage the administrator's profile.
     */
    Administrateur admin;

    /**
     * A map that defines the dependencies between competencies.
     * Each CheckBox is associated with a list of CheckBoxes that must be checked if it is checked.
     */
    private final Map<CheckBox, List<CheckBox>> dependances = new HashMap<>();

    /**
     * A map that defines the reverse dependencies for competencies.
     * Each CheckBox is associated with a list of CheckBoxes that depend on it.
     * This is used to uncheck dependent competencies when a competency is unchecked.
     */
    private final Map<CheckBox, List<CheckBox>> reverseDependances = new HashMap<>();

    /**
     * The MenuParalleleController that manages the parallel menu in the application.
     * This controller is used to switch between different views in the parallel menu.
     */
    private MenuParalleleController menuParalleleController;

    /**
     * The AnchorPane that serves as the settings pane.
     * This pane contains all the UI elements for managing user settings.
     */
    @FXML private AnchorPane settingsPane;

    /**
     * The Text that displays the user's name in the settings.
     * This text is updated based on the current user's profile.
     */
    @FXML private Text prenomNomParam;

    /**
     * The Circle that displays the user's profile picture.
     * This circle is updated when the user uploads a new profile picture.
     */
    @FXML private Circle pdpParamCircle;

    /**
     * CheckBox for PSE1 competency
     */
    @FXML private CheckBox checkPSE1;

    /**
     * CheckBox for PSE2 competency
     */
    @FXML private CheckBox checkPSE2;

    /**
     * CheckBox for CE competency
     */
    @FXML private CheckBox checkCE;

    /**
     * CheckBox for CP competency
     */
    @FXML private CheckBox checkCP;

    /**
     * CheckBox for CO competency
     */
    @FXML private CheckBox checkCO;

    /**
     * CheckBox for SSA competency
     */
    @FXML private CheckBox checkSSA;

    /**
     * CheckBox for VPSP competency
     */
    @FXML private CheckBox checkVPSP;

    /**
     * CheckBox for PBC competency
     */
    @FXML private CheckBox checkPBC;

    /**
     * CheckBox for PBF competency
     */
    @FXML private CheckBox checkPBF;

    /**
     * One of the lines that visually separates sections in the settings pane.
     */
    @FXML private Line line;

    /**
     * Text for competencies section for rescuers.
     * This text is hidden for administrators.
     */
    @FXML private Text compText;

    /**
     * Text for certifications section for rescuers.
     * This text is hidden for administrators.
     */
    @FXML private Text certifText;

    /**
     * The Pane that serves as the background for the settings pane.
     * This pane is styled with a gradient background for visual appeal.
     */
    @FXML private Pane colorPane;

    /**
     * The ImageView that displays a cross icon for closing the settings pane.
     * This icon is used to close the settings view.
     */
    @FXML private ImageView croix;

    /**
     * A byte array to hold the new profile picture uploaded by the user.
     * This is used to store the image data before saving it to the database.
     */
    private byte[] nouvellePhoto;


    /**
     * CheckBox for enabling greedy mode.
     */
    @FXML private CheckBox greedyCheckBox;

    /**
     * Initializes the DashboardSettingsController by setting up the user profile and competencies.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        try {
            sec = secouristeManagement.getSecouristeById(getInstanceAuthentificationManagement().getCurrentUser().getIdUser());
            admin = administrateurDAO.findById(getInstanceAuthentificationManagement().getCurrentUser().getIdUser());
            if(getInstanceAuthentificationManagement().isAdmin()){
                // Appliquer le fond dégradé pour l'admin
                colorPane.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 0%,  #BFB0FE, #3636E1); -fx-background-radius:  30 30 0 0;");

                prenomNomParam.setText(admin.getPrenom() + " " + admin.getNom());
                if (admin.getPhoto() != null) {
                    Image image = new Image(new ByteArrayInputStream(admin.getPhoto()));
                    pdpParamCircle.setFill(new ImagePattern(image));
                }
                checkPSE1.setVisible(false);
                checkPSE2.setVisible(false);
                checkCE.setVisible(false);
                checkCP.setVisible(false);
                checkCO.setVisible(false);
                checkSSA.setVisible(false);
                checkVPSP.setVisible(false);
                checkPBC.setVisible(false);
                checkPBF.setVisible(false);
                line.setVisible(false);
                compText.setVisible(false);
                certifText.setVisible(false);

                greedyCheckBox.setVisible(true);

                if (model.utils.Settings.useGreedy()) {
                    greedyCheckBox.setSelected(true);
                } else {
                    greedyCheckBox.setSelected(false);
                }
            } else {
                prenomNomParam.setText(sec.getPrenom() + " " + sec.getNom());
                if (sec.getPhoto() != null) {
                    Image image = new Image(new ByteArrayInputStream(sec.getPhoto()));
                    pdpParamCircle.setFill(new ImagePattern(image));
                }
                PossessionManagement possessionManagement = new PossessionManagement();
                Possession possession = possessionManagement.getPossessionBySecouriste(sec);

                checkPSE1.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "PSE1".equals(c.getIntitule())));
                checkPSE2.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "PSE2".equals(c.getIntitule())));
                checkCE.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "CE".equals(c.getIntitule())));
                checkCP.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "CP".equals(c.getIntitule())));
                checkCO.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "CO".equals(c.getIntitule())));
                checkSSA.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "SSA".equals(c.getIntitule())));
                checkPBC.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "PBC".equals(c.getIntitule())));
                checkPBF.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "PBF".equals(c.getIntitule())));
                checkVPSP.setSelected(possession.getCompetencesSec().stream().anyMatch(c -> "VPSP".equals(c.getIntitule())));
            }
        } catch (Exception e) {

        }

        // Définir les dépendances "vers le bas" (cocher les prérequis)
        dependances.put(checkCO, List.of(checkCP));
        dependances.put(checkCP, List.of(checkCE));
        dependances.put(checkCE, List.of(checkPSE2));
        dependances.put(checkPSE2, List.of(checkPSE1));
        dependances.put(checkSSA, List.of(checkPSE1));
        dependances.put(checkVPSP, List.of(checkPSE2));
        dependances.put(checkPBF, List.of(checkPBC));

        // Construire la map inverse "vers le haut"
        for (Map.Entry<CheckBox, List<CheckBox>> entry : dependances.entrySet()) {
            for (CheckBox prerequis : entry.getValue()) {
                reverseDependances.computeIfAbsent(prerequis, k -> new ArrayList<>()).add(entry.getKey());
            }
        }

        // Listener sur tous les checkboxes
        Set<CheckBox> allCheckboxes = new HashSet<>();
        allCheckboxes.addAll(dependances.keySet());
        dependances.values().forEach(allCheckboxes::addAll);

        for (CheckBox cb : allCheckboxes) {
            cb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    cocherDependenciesRecursivement(cb);
                } else {
                    decocherSuperieursRecursivement(cb);
                }
            });
        }
    }

    /**
     * Retrieves the competencies selected by the user.
     * This method collects all competencies that are currently checked and returns them as an ArrayList.
     *
     * @return An ArrayList of Competence objects representing the selected competencies.
     */
    private ArrayList<Competence> getSelectedCompetences() {
        ArrayList<Competence> competences = new ArrayList<>();
        if (checkPSE1.isSelected()) competences.add(new Competence("PSE1"));
        if (checkPSE2.isSelected()) competences.add(new Competence("PSE2"));
        if (checkCE.isSelected()) competences.add(new Competence("CE"));
        if (checkCP.isSelected()) competences.add(new Competence("CP"));
        if (checkCO.isSelected()) competences.add(new Competence("CO"));
        if (checkSSA.isSelected()) competences.add(new Competence("SSA"));
        if (checkVPSP.isSelected()) competences.add(new Competence("VPSP"));
        if (checkPBC.isSelected()) competences.add(new Competence("PBC"));
        if (checkPBF.isSelected()) competences.add(new Competence("PBF"));
        return competences;
    }

    /**
     * Recursively checks all dependent competencies when a competency is checked.
     * This method ensures that all prerequisites are automatically checked when a competency is selected.
     *
     * @param checkBox The CheckBox representing the competency that was checked.
     */
    private void cocherDependenciesRecursivement(CheckBox checkBox) {
        List<CheckBox> deps = dependances.get(checkBox);
        if (deps != null) {
            for (CheckBox dep : deps) {
                if (!dep.isSelected()) {
                    dep.setSelected(true);
                    cocherDependenciesRecursivement(dep); // appel récursif
                }
            }
        }
    }

    /**
     * Recursively unchecks all superior competencies when a competency is unchecked.
     * This method ensures that if a prerequisite is unchecked, all competencies that depend on it are also unchecked.
     *
     * @param checkBox The CheckBox representing the competency that was unchecked.
     */
    private void decocherSuperieursRecursivement(CheckBox checkBox) {
        List<CheckBox> superieurs = reverseDependances.get(checkBox);
        if (superieurs != null) {
            for (CheckBox sup : superieurs) {
                // On ne décoche que si le prérequis qu'on vient de décocher est nécessaire
                boolean doitDecocher = dependances.getOrDefault(sup, List.of())
                        .contains(checkBox) && !checkBox.isSelected();
                if (doitDecocher && sup.isSelected()) {
                    sup.setSelected(false);
                    decocherSuperieursRecursivement(sup); // appel récursif
                }
            }
        }
    }

    /**
     * Handles the import of a new profile picture.
     * This method opens a file chooser dialog to select an image file and updates the profile picture in the UI.
     * The selected image is stored in the nouvellePhoto byte array for later saving to the database.
     */
    @FXML
    private void importerPdp() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une photo de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                Image image = new Image(fis);

                // Met à jour l'image dans le FXML uniquement (pas en base)
                pdpParamCircle.setFill(new ImagePattern(image));

                // Remettre le curseur au début pour relire le fichier
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    FileInputStream fis2 = new FileInputStream(selectedFile);
                    fis2.transferTo(baos);
                    nouvellePhoto = baos.toByteArray();
                }

                System.out.println("Photo chargée avec succès (non enregistrée en base).");

            } catch (IOException e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }
    }

    /**
     * Handles the click event for the "Enregistrer" button.
     * This method saves the new profile picture and updates the competencies for the current user.
     * It also navigates back to the profile display page after saving.
     */
    @FXML
    private void enregistrerClicked() {
        if(getInstanceAuthentificationManagement().isAdmin()){
            long idAdmin = getInstanceAuthentificationManagement().getCurrentUser().getIdUser();
            if (nouvellePhoto != null) {
                boolean success = administrateurManagement.updatePhoto(idAdmin, nouvellePhoto);
                System.out.println("Nouvelle photo enregistrée en base.");
            } else {
                System.out.println("Aucune nouvelle photo à enregistrer.");
            }
            menuParalleleController.setRow(0, "/fxml/common/DisplayProfil.fxml");
            initialize();
        } else {
            long idSec = getInstanceAuthentificationManagement().getCurrentUser().getIdUser();
            if (nouvellePhoto != null) {
                boolean success = secouristeManagement.updatePhoto(idSec, nouvellePhoto);
                System.out.println("Nouvelle photo enregistrée en base.");
            } else {
                System.out.println("Aucune nouvelle photo à enregistrer.");
            }

            // Récupérer compétences sélectionnées
            ArrayList<Competence> competencesSelectionnees = getSelectedCompetences();
            Possession possession = new Possession(competencesSelectionnees, sec);
            PossessionDAO possessionDAO = new PossessionDAO();
            possessionDAO.deleteAllPossessionsForSecouriste(idSec);
            possessionDAO.insert(possession);
            menuParalleleController.setRow(0, "/fxml/common/DisplayProfil.fxml");
            initialize();
        }
    }

    /**
     * Handles the click event for the "Annuler" button.
     * This method resets the settings page to its initial state without saving any changes.
     */
    @FXML
    private void annulerClicked() {
        initialize();
    }


    /**
     * Handles the click event for the "Fermer" button.
     * This method navigates back to the dashboard calendar assignment page.
     */
    @FXML
    private void fermerClicked() {

        if (fenetreGestionController != null) {
            fenetreGestionController.loadContent2("/fxml/common/DashboardCalendarAssignment.fxml");
        }

        if (menuParalleleController != null) {
            menuParalleleController.linkToSettings();
        } else {
            System.out.println("menuParalleleController est null !");
        }
    }

    /**
     * Handles the click event for the "Supprimer" button.
     * This method removes the current user (either admin or rescuer) and navigates to the login page.
     * If the user is an admin, it removes the administrator; otherwise, it removes the rescuer.
     */
    @FXML
    private void supprimerClicked() {
        if(getInstanceAuthentificationManagement().isAdmin()){
            UtilsController.linkToPage(fenetreGestionController.getFenetreGestion(), "/fxml/auth/Login.fxml");
            long idAdmin = getInstanceAuthentificationManagement().getCurrentUser().getIdUser();
            administrateurManagement.removeAdministrateur(admin);
        } else {
            UtilsController.linkToPage(fenetreGestionController.getFenetreGestion(), "/fxml/auth/Login.fxml");
            PossessionDAO possessionDAO = new PossessionDAO();
            DisponibiliteDAO disponibiliteDAO = new DisponibiliteDAO();
            long idSec = getInstanceAuthentificationManagement().getCurrentUser().getIdUser();
            possessionDAO.deleteAllPossessionsForSecouriste(idSec);
            disponibiliteDAO.deleteAllDisponibilites(idSec);
            secouristeManagement.removeSecouriste(sec);
        }
    }

    /**
     * Handles the click event for the "Déconnecter" button.
     * This method logs out the current user, closes the current window,
     * and relaunches the application to ensure a clean state.
     */
    @FXML
    private void deconnecterClicked() {
        // Déconnexion de l'utilisateur
        getInstanceAuthentificationManagement().logOut();

        try {
            // FClose the current window
            javafx.stage.Stage stage = (javafx.stage.Stage) fenetreGestionController.getFenetreGestion().getScene().getWindow();

            // Create a new instance of the main application
            javafx.application.Platform.runLater(() -> {
                try {
                    // Lancer une nouvelle instance de l'application
                    view.MainApp mainApp = new view.MainApp();
                    mainApp.start(new javafx.stage.Stage());

                    // CLose the current stage
                    stage.close();
                } catch (Exception e) {
                    System.err.println("Erreur lors du redémarrage de l'application : " + e.getMessage());
                    e.printStackTrace();

                    // If an error occurs, fallback to the login page
                    UtilsController.linkToPage(fenetreGestionController.getFenetreGestion(), "/fxml/auth/Login.fxml");
                }
            });
        } catch (Exception e) {
            System.err.println("Erreur lors de la déconnexion : " + e.getMessage());
            e.printStackTrace();

            // Fallback if error occurs
            UtilsController.linkToPage(fenetreGestionController.getFenetreGestion(), "/fxml/auth/Login.fxml");
        }
    }

/**
     * Sets the FenetreGestionController for this DashboardSettingsController.
     * This method is used to inject the main application controller into this controller.
     *
     * @param fenetreGestionController The FenetreGestionController to set.
     */
    public void setFenetreGestionController(FenetreGestionController fenetreGestionController) {
        this.fenetreGestionController = fenetreGestionController;
    }

    /**
     * Sets the MenuParalleleController for this DashboardSettingsController.
     * This method is used to inject the parallel menu controller into this controller.
     *
     * @param menuParalleleController The MenuParalleleController to set.
     */
    public void setMenuParalleleController(MenuParalleleController menuParalleleController) {
        this.menuParalleleController = menuParalleleController;
    }

    public void greedyCheckBoxToggled(ActionEvent actionEvent) {
        boolean isGreedy = greedyCheckBox.isSelected();
        System.out.println("[*] Settings useGreedy: " + isGreedy);
        model.utils.Settings.setUseGreedy(isGreedy);
        if (isGreedy) {
            System.out.println("[*] Greedy mode enabled.");
        } else {
            System.out.println("[*] Greedy mode disabled.");
        }
    }
}
