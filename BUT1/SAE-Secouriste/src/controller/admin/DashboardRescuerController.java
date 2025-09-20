package controller.admin;

import controller.layoutmanager.FenetreGestionController;
import controller.layoutmanager.FenetreGestionInjectable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.data.persistence.*;
import model.data.service.AffectationManagement;
import model.data.service.PossessionManagement;
import model.data.service.SecouristeManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static controller.layoutmanager.FenetreGestionController.showOverlay;

/**
 * Controller for managing rescuers in the administration interface.
 * This class handles the display and management of rescuers, including their certifications and group assignments.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DashboardRescuerController implements FenetreGestionInjectable {

    /**
     * The TilePane that displays the rescuers.
     */
    @FXML
    private TilePane evenementTile;

    /**
     * The ComboBox for selecting groups of rescuers.
     */
    @FXML
    private ComboBox<String> grpComboBox;

    /**
     * The ComboBox for selecting certifications of rescuers.
     */
    @FXML
    private ComboBox<String> certComboBox;

    /**
     * The AnchorPane that contains the rescuer management interface.
     */
    @FXML
    private AnchorPane gestionSecouriste;

    /**
     * List of groups that rescuers can belong to.
     */
    private ArrayList<String> groupes;

    /**
     * List of certifications that rescuers can have.
     */
    private ArrayList<String> certifications;

    /**
     * List of certifications that rescuers are currently attending.
     */
    private ArrayList<String> attCertifications;

    /**
     * List of rescuers currently managed in the interface.
     */
    private List<Secouriste> secouristeList;

    /**
     * Reference to the FenetreGestionController for managing the overall administration interface.
     */
    private FenetreGestionController fenetreGestionController;

    /**
     * Management service for rescuers.
     */
    private final SecouristeManagement secouristeManagement = new SecouristeManagement();

    /**
     * Management service for possessions of rescuers.
     */
    private final PossessionManagement possessionManagement = new PossessionManagement();

    /**
     * Management service for assignments of rescuers to groups.
     */
    private final AffectationManagement affectationManagement = new AffectationManagement();

    /**
     * Initializes the controller and sets up the rescuers' management interface.
     * This method is called automatically by JavaFX when the FXML file is loaded.
     */
    @FXML
    public void initialize() {

        this.groupes = new ArrayList<>();
        this.certifications = new ArrayList<>();
        this.attCertifications = new ArrayList<>();

        this.evenementTile.setHgap(50);
        this.evenementTile.setVgap(50);
        this.evenementTile.setStyle("-fx-padding: 50;");
        this.evenementTile.setMaxWidth(1350);

        this.secouristeList = secouristeManagement.getSecouristes();

        tileInitialize(this.secouristeList);
        comboBoxInitialize();
    }

    /**
     * Initializes the rescuers' tiles with the provided list of rescuers.
     * This method creates a visual representation of each rescuer, including their certifications and group assignments.
     *
     * @param listSecouristes The list of rescuers to be displayed in the interface.
     */
    private void tileInitialize(List<Secouriste> listSecouristes) {
        if (listSecouristes != null) {
            for (Secouriste secouriste : listSecouristes) {

                Possession possession = this.possessionManagement.getPossessionBySecouriste(secouriste);
                if (possession != null) {
                    for (Competence competence : possession.getCompetencesSec()) {
                        if (!this.certifications.contains(competence.getIntitule())) {
                            this.certifications.add(competence.getIntitule());
                        }
                    }
                }

                List<Affectation> affectations = this.affectationManagement.getAffectationsByRescuer(secouriste);
                for (Affectation affectation : affectations) {
                    if (!this.groupes.contains(affectation.getDPSAffect().getName())) {
                        this.groupes.add(affectation.getDPSAffect().getName());
                    }
                }

                GridPane gridPane = new GridPane();
                gridPane.setPrefSize(369, 376);
                gridPane.setMaxSize(369, 376);
                gridPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10; -fx-border-color: #A0A0A0; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");

                Label personne = new Label(secouriste.getNom() + " " + secouriste.getPrenom());
                personne.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

                GridPane subSubGridPane1 = new GridPane();
                subSubGridPane1.setPrefSize(230, 22);
                subSubGridPane1.setMaxSize(230, 22);

                Label certif = new Label("Certifications :");
                certif.setStyle("-fx-text-fill: #FFFFFF;");

                int row = 0;
                int col = 0;
                for (Competence competence : possession.getCompetencesSec()) {
                    Label certLabel = new Label(competence.getIntitule());
                    certLabel.setMinWidth(50);
                    certLabel.setAlignment(Pos.CENTER);
                    GridPane.setMargin(certLabel, new Insets(5));
                    certLabel.setStyle("-fx-border-radius: 20; -fx-border-color: #FFFFFF; -fx-text-fill: #FFFFFF; -fx-border-width: 2px; -fx-padding: 6;");
                    if (row == 5) {
                        row = 0;
                        col++;
                    }
                    subSubGridPane1.add(certLabel, row, col);
                    row++;
                }

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setFitToWidth(true);
                scrollPane.setMaxHeight(Double.MAX_VALUE);
                scrollPane.setMaxWidth(Double.MAX_VALUE);
                GridPane.setVgrow(scrollPane, Priority.ALWAYS);
                GridPane.setHgrow(scrollPane, Priority.ALWAYS);
                scrollPane.setStyle("-fx-background: trensparent; -fx-background-color: trensparent; -fx-border-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");

                GridPane subSubGridPane2 = new GridPane();
                subSubGridPane2.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                subSubGridPane2.setStyle("-fx-background-color: trensparent;");

                Label affect = new Label("Affectations :");
                affect.setStyle("-fx-text-fill: #FFFFFF;");

                row = 0;
                col = 0;
                for (Affectation affectation : this.affectationManagement.getAffectationsByRescuer(secouriste)) {
                    Label affectLabel = new Label(affectation.getDPSAffect().getName() + " (" + affectation.getCompetenceAffect().getIntitule() + ") : " + affectation.getDPSAffect().getJournee());
                    affectLabel.setMinWidth(295);
                    affectLabel.setAlignment(Pos.CENTER);
                    GridPane.setMargin(affectLabel, new Insets(5));
                    affectLabel.setStyle("-fx-border-radius: 20; -fx-border-color: #FFFFFF; -fx-text-fill: #FFFFFF; -fx-border-width: 2px; -fx-padding: 6;");
                    col++;
                    subSubGridPane2.add(affectLabel, row, col);
                }

                scrollPane.setContent(subSubGridPane2);

                GridPane subGridPane1 = new GridPane();
                subGridPane1.setPrefSize(354, 305);
                subGridPane1.setMaxSize(354, 305);
                subGridPane1.setStyle("-fx-background-color: rgb(231,94,94); -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10;");

                Region spacer = new Region();
                spacer.setMinHeight(20);
                Region spacer2 = new Region();
                spacer2.setMinHeight(20);

                subGridPane1.add(personne, 0, 0);
                subGridPane1.add(spacer, 0, 1);
                subGridPane1.add(certif, 0, 2);
                subGridPane1.add(subSubGridPane1, 0, 3);
                subGridPane1.add(spacer2, 0, 4);
                subGridPane1.add(affect, 0, 5);
                subGridPane1.add(scrollPane, 0, 6);

                GridPane subGridPane2 = new GridPane();
                subGridPane2.setPrefSize(354, 71);
                subGridPane2.setMaxSize(354, 71);

                Button adminButton = new Button("Mettre administrateur");
                adminButton.setMinWidth(125);
                adminButton.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10; -fx-font-size: 15; -fx-border-color: #000000; -fx-border-width: 2px; -fx-text-fill: #000000;");
                Button supprButton = new Button("Suppression");
                supprButton.setMinWidth(125);
                supprButton.setStyle("-fx-background-color: rgb(231,94,94); -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10; -fx-font-size: 15; -fx-text-fill: #FFFFFF;");

                adminButton.setOnAction(event -> setAdminSecouriste(secouriste));
                supprButton.setOnAction(event -> supprSecouriste(secouriste));

                GridPane buttonGridPane = new GridPane();
                buttonGridPane.setAlignment(Pos.CENTER_RIGHT);
                buttonGridPane.setHgap(10);
                buttonGridPane.add(adminButton, 0, 0);
                buttonGridPane.add(supprButton, 1, 0);

                subGridPane2.add(buttonGridPane, 0, 0);
                subGridPane2.setAlignment(Pos.CENTER_RIGHT);
                GridPane.setMargin(buttonGridPane, new Insets(10, 0, 0, 0));

                gridPane.add(subGridPane1, 0, 0);
                gridPane.add(subGridPane2, 0, 1);

                this.evenementTile.getChildren().add(gridPane);
            }

            for (int i = 0; i < (3 - listSecouristes.size() % 3); i++) {
                GridPane emptyPane = new GridPane();
                emptyPane.setPrefSize(369, 367);
                emptyPane.setMaxSize(369, 376);
                emptyPane.setStyle("-fx-background-color: #EBF0F6; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10;");
                this.evenementTile.getChildren().add(emptyPane);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                GridPane emptyPane = new GridPane();
                emptyPane.setPrefSize(369, 367);
                emptyPane.setMaxSize(369, 376);
                emptyPane.setStyle("-fx-background-color: #EBF0F6; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10;");
                this.evenementTile.getChildren().add(emptyPane);
            }
        }
    }

    /**
     * Sets a rescuer as an administrator.
     * This method opens a popup to confirm the action and updates the rescuer's status accordingly.
     *
     * @param secouriste The rescuer to be set as an administrator.
     */
    private void setAdminSecouriste(Secouriste secouriste) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/PopupRescuerSetAdmin.fxml"));
            Parent overlayContent = loader.load();

            PopupRescuerSetAdminController controller = loader.getController();
            controller.initializeSetAdminSecouriste(secouriste, this);

            StackPane overlayPane = showOverlay();

            overlayPane.getChildren().add(overlayContent);
            StackPane.setAlignment(overlayContent, Pos.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a popup to confirm the deletion of a rescuer.
     * This method allows the administrator to remove a rescuer from the system.
     *
     * @param secouriste The rescuer to be deleted.
     */
    private void supprSecouriste(Secouriste secouriste) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/PopupDeleteRescuer.fxml"));
            Parent overlayContent = loader.load();

            PopupDeleteRescuerController controller = loader.getController();
            controller.initializeSupprSecouriste(secouriste, this);

            StackPane overlayPane = showOverlay();

            overlayPane.getChildren().add(overlayContent);
            StackPane.setAlignment(overlayContent, Pos.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a rescuer as an administrator.
     * This method is a placeholder for the actual implementation of setting a rescuer as an admin.
     *
     * @param secouriste The rescuer to be set as an administrator.
     */
    private void setAdmin(Secouriste secouriste) {
    }

    /**
     * Initializes the ComboBoxes for selecting groups and certifications.
     * This method populates the ComboBoxes with available groups and certifications.
     */
    private void comboBoxInitialize() {
        this.grpComboBox.getItems().add("Groupe d'affectation");
        this.certComboBox.getItems().add("Certification");
        this.grpComboBox.getSelectionModel().select("Groupe d'affectation");
        this.certComboBox.getSelectionModel().select("Certification");
        this.grpComboBox.getItems().addAll(this.groupes);
        this.certComboBox.getItems().addAll(this.certifications);
    }

    /**
     * Updates the displayed rescuers based on the selected filters in the ComboBoxes.
     * This method filters the list of rescuers based on their certifications and group assignments.
     */
    @FXML
    public void filtreUpdate() {
        this.evenementTile.getChildren().clear();
        ArrayList<Secouriste> listSecouriste = new ArrayList<>();
        for (Secouriste secouriste : this.secouristeList) {
            boolean verifCert = false;
            if (this.certComboBox.getSelectionModel().getSelectedItem() != null && this.certComboBox.getSelectionModel().getSelectedItem().equals("Certification")) {
                verifCert = true;
            } else {
                for (Competence competence : this.possessionManagement.getPossessionBySecouriste(secouriste).getCompetencesSec()) {
                    if (competence.getIntitule().equals(this.certComboBox.getSelectionModel().getSelectedItem())) {
                        System.out.println(this.certComboBox.getSelectionModel().getSelectedItem());
                        verifCert = true;
                    }
                }
            }

            boolean verifGrp = false;
            if (this.grpComboBox.getSelectionModel().getSelectedItem() != null && this.grpComboBox.getSelectionModel().getSelectedItem().equals("Groupe d'affectation")) {
                verifGrp = true;
            } else {
                for (Affectation affectation : this.affectationManagement.getAffectationsByRescuer(secouriste)) {
                    if (affectation.getDPSAffect().getName().equals(this.grpComboBox.getSelectionModel().getSelectedItem())) {
                        System.out.println(this.grpComboBox.getSelectionModel().getSelectedItem());
                        verifGrp = true;
                    }
                }
            }

            if (verifCert && verifGrp) {
                listSecouriste.add(secouriste);
            }

        }
        tileInitialize(listSecouriste);
    }

    /**
     * Sets the FenetreGestionController for this controller.
     * This method is used to inject the main administration controller into this controller.
     *
     * @param fenetreGestionController The FenetreGestionController to be set.
     */
    public void setFenetreGestionController(FenetreGestionController fenetreGestionController) {
        this.fenetreGestionController = fenetreGestionController;
    }

    /**
     * Loads the event management interface.
     * This method is called when the "Gestion Evenement" button is clicked.
     */
    @FXML
    public void GestionEvenementButton() {
        this.fenetreGestionController.loadContent2("/fxml/admin/DashboardEvent.fxml");
    }

    /**
     * Removes a rescuer from the list of managed rescuers.
     * @param secouriste
     */
    public void retirerList(Secouriste secouriste) {
        this.secouristeList.remove(secouriste);
    }
}