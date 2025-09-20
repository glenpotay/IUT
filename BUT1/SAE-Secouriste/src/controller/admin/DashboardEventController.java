package controller.admin;

import controller.common.DisplayUpComingEventController;
import controller.layoutmanager.FenetreGestionController;
import controller.layoutmanager.FenetreGestionInjectable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import model.data.persistence.DPS;
import model.data.service.AffectationManagement;
import model.data.service.BesoinManagement;
import model.data.service.DPSManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static controller.layoutmanager.FenetreGestionController.showOverlay;

/**
 * Controller for managing events in the application.
 * This controller handles the display and management of DPS (Données de Prise en Secours) events.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DashboardEventController implements FenetreGestionInjectable {

    /**
     * The tile pane that displays the events.
     */
    @FXML
    private TilePane evenementTile;

    /**
     * The combo box for selecting sites.
     */
    @FXML
    private ComboBox<String> sitesComboBox;

    /**
     * The combo box for selecting sports.
     */
    @FXML
    private ComboBox<String> sportsComboBox;

    /**
     * The anchor pane that contains the event management UI.
     */
    @FXML
    private AnchorPane gestionEvenement;

    /**
     * List of sports associated with the events.
     */
    private ArrayList<String> sports;

    /**
     * List of sites associated with the events.
     */
    private ArrayList<String> sites;

    /**
     * List of DPS events to be displayed.
     */
    private List<DPS> dpsList;

    /**
     * The controller for managing the main application window.
     */
    private FenetreGestionController fenetreGestionController;

    /**
     * The controller for managing individual events.
     */
    private DisplayUpComingEventController evenementController;

    /**
     * Management service for DPS events.
     */
    private final DPSManagement dpsManagement = new DPSManagement();

    /**
     * Management service for affectations related to DPS events.
     */
    private final AffectationManagement affectationManagement = new AffectationManagement();

    /**
     * Management service for needs related to DPS events.
     */
    private final BesoinManagement besoinManagement = new BesoinManagement();

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize() {
        System.out.println("DashboardEventController initialized");
        this.sports = new ArrayList<>();
        this.sites = new ArrayList<>();
        this.dpsList = new ArrayList<>();

        this.evenementTile.setHgap(50);
        this.evenementTile.setVgap(50);
        this.evenementTile.setStyle("-fx-padding: 50;");
        this.evenementTile.setMaxWidth(1350);

        this.dpsList = this.dpsManagement.getDps();

        tileInitialize(this.dpsList);
        comboBoxInitialize();
    }

    /**
     * Initializes the tile pane with the list of DPS events.
     *
     * @param listDPS The list of DPS events to be displayed.
     */
    private void tileInitialize(List<DPS> listDPS) {

        String cssPath = getClass().getResource("/css/button.css").toExternalForm();
        gestionEvenement.getStylesheets().add(cssPath);


        String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

        for (DPS dps : listDPS) {
            if (!this.sports.contains(dps.getSport().getNom())) {
                this.sports.add(dps.getSport().getNom());
            }

            if (!this.sites.contains(dps.getSite().getNom())) {
                this.sites.add(dps.getSite().getNom());
            }

            GridPane gridPane = new GridPane();
            gridPane.setPrefSize(369, 376);
            gridPane.setMaxSize(369, 376);
            gridPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10; -fx-border-color: #A0A0A0; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");

            Label titre = new Label(dps.getName());
            titre.setStyle("-fx-font-size: 32; -fx-font-weight: bold;");

            Label sport = new Label("Sport : " + dps.getSport().getNom());
            sport.setStyle("-fx-font-size: 15;");

            Label site = new Label("Lieu : " + dps.getSite().getNom());
            site.setStyle("-fx-font-size: 15;");

            GridPane subSubGridPane = new GridPane();
            subSubGridPane.setPrefSize(230, 22);
            subSubGridPane.setMaxSize(230, 22);

            Label jour = new Label(dps.getJournee().getJour() + " " + months[dps.getJournee().getMois() - 1] + " : ");
            Label dep = new Label(dps.getHoraireDepart() + "h");
            Label entre = new Label("au");
            Label fin = new Label(dps.getHoraireFin() + "h");
            jour.setMinWidth(115);
            dep.setMinWidth(75);
            entre.setMinWidth(50);
            fin.setMinWidth(75);
            jour.setAlignment(Pos.CENTER);
            dep.setAlignment(Pos.CENTER);
            entre.setAlignment(Pos.CENTER);
            fin.setAlignment(Pos.CENTER);
            jour.setStyle("-fx-font-size: 15;");
            dep.setStyle("-fx-border-radius: 20; -fx-border-color: #000000; -fx-border-width: 1px; -fx-padding: 6; -fx-font-size: 15;");
            entre.setStyle("-fx-font-size: 15;");
            fin.setStyle("-fx-border-radius: 20; -fx-border-color: #000000; -fx-border-width: 1px; -fx-padding: 6; -fx-font-size: 15;");

            subSubGridPane.add(jour, 0, 0);
            subSubGridPane.add(dep, 1, 0);
            subSubGridPane.add(entre, 2, 0);
            subSubGridPane.add(fin, 3, 0);

            GridPane subGridPane1 = new GridPane();
            subGridPane1.setPrefSize(354, 305);
            subGridPane1.setMaxSize(354, 305);
            subGridPane1.setStyle("-fx-background-color: #B9D9FF; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10;");

            HBox subSubHbox = new HBox();
            subSubHbox.setAlignment(Pos.CENTER);
            subSubHbox.setPrefWidth(354);

            Label membresAttr = new Label("Affectés : " + this.affectationManagement.getIdRescuersByDps(dps.getId()).size());
            membresAttr.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
            membresAttr.setAlignment(Pos.CENTER);
            membresAttr.setMaxWidth(Double.MAX_VALUE);

            Label membresNonAttr = new Label("Non affectés : " + this.besoinManagement.getBesoinByDPS(dps).getCompetences().size());
            membresNonAttr.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
            membresNonAttr.setAlignment(Pos.CENTER);
            membresNonAttr.setMaxWidth(Double.MAX_VALUE);

            HBox.setHgrow(membresAttr, Priority.ALWAYS);
            HBox.setHgrow(membresNonAttr, Priority.ALWAYS);

            subSubHbox.getChildren().addAll(membresAttr, membresNonAttr);

            Region spacer1 = new Region();
            spacer1.setMinHeight(10);

            Region spacer2 = new Region();
            spacer2.setMinHeight(5);

            Region spacer3 = new Region();
            spacer3.setMinHeight(15);

            Region spacer4 = new Region();
            spacer4.setMinHeight(65);

            subGridPane1.add(titre, 0, 0);
            subGridPane1.add(spacer1, 0, 1);
            subGridPane1.add(sport, 0, 2);
            subGridPane1.add(spacer2, 0, 3);
            subGridPane1.add(site, 0, 4);
            subGridPane1.add(spacer3, 0, 5);
            subGridPane1.add(subSubGridPane, 0, 6);
            subGridPane1.add(spacer4, 0, 7);
            subGridPane1.add(subSubHbox, 0, 8);

            GridPane subGridPane2 = new GridPane();
            subGridPane2.setPrefSize(354, 71);
            subGridPane2.setMaxSize(354, 71);

            Button modifButton = new Button("Modification");
            modifButton.setMinWidth(110);
            modifButton.setStyle("-fx-background-radius: 20, 19; -fx-padding: 10; -fx-font-size: 15; -fx-background-color: #000000, white; -fx-background-insets: 0, 1; -fx-border-width: 0; -fx-text-fill: #000000;");
            modifButton.getStyleClass().add("button");

            Button majButton = new Button("Mise à jour");
            majButton.setMinWidth(110);
            majButton.setStyle("-fx-background-radius: 20, 19; -fx-padding: 10; -fx-font-size: 15; -fx-background-color: #000000, white; -fx-background-insets: 0, 1; -fx-border-width: 0; -fx-text-fill: #000000;");
            majButton.getStyleClass().add("button");

            Button supprButton = new Button("Suppression");
            supprButton.setMinWidth(110);
            supprButton.setStyle("-fx-background-color: #FF004D; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10; -fx-font-size: 15; -fx-border-width: 2px; -fx-text-fill: #FFFFFF;");
            supprButton.getStyleClass().add("button");

            modifButton.setOnAction(event -> modifAffect(dps));
            majButton.setOnAction(event -> majAffect(dps));
            supprButton.setOnAction(event -> supprDps(dps));

            GridPane buttonGridPane = new GridPane();
            buttonGridPane.setAlignment(Pos.CENTER_RIGHT);
            buttonGridPane.setHgap(10);
            buttonGridPane.add(modifButton, 0, 0);
            buttonGridPane.add(supprButton, 2, 0);

            if (!this.besoinManagement.getBesoinByDPS(dps).getCompetences().isEmpty()) {
                buttonGridPane.add(majButton, 1, 0);
            }

            subGridPane2.add(buttonGridPane, 0, 0);
            subGridPane2.setAlignment(Pos.CENTER_RIGHT);
            GridPane.setMargin(buttonGridPane, new Insets(10, 0, 0, 0));

            gridPane.add(subGridPane1, 0, 0);
            gridPane.add(subGridPane2, 0, 1);

            this.evenementTile.getChildren().add(gridPane);
        }

        for (int i = 0; i < (3 - listDPS.size() % 3); i++) {
            GridPane emptyPane = new GridPane();
            emptyPane.setPrefSize(369, 367);
            emptyPane.setMaxSize(369, 376);
            emptyPane.setStyle("-fx-background-color: #EBF0F6; -fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10;");
            this.evenementTile.getChildren().add(emptyPane);
        }
    }

    /**
     * Initializes the combo boxes for filtering events by site and sport.
     */
    public void comboBoxInitialize() {
        this.sitesComboBox.getItems().add("Site");
        this.sportsComboBox.getItems().add("Sport");
        this.sitesComboBox.getSelectionModel().select("Site");
        this.sportsComboBox.getSelectionModel().select("Sport");
        this.sitesComboBox.getItems().addAll(this.sites);
        this.sportsComboBox.getItems().addAll(this.sports);
    }

    /**
     * Updates the displayed events based on the selected filters in the combo boxes.
     * Clears the current tile pane and repopulates it with filtered DPS events.
     */
    @FXML
    public void filtreUpdate() {
        this.evenementTile.getChildren().clear();
        ArrayList<DPS> listDPS = new ArrayList<>();
        for (DPS dps : this.dpsManagement.getDps()) {
            if ((dps.getSite().getNom().equals(this.sitesComboBox.getSelectionModel().getSelectedItem())
                    || this.sitesComboBox.getSelectionModel().getSelectedItem().equals("Site"))
                    && (dps.getSport().getNom().equals(this.sportsComboBox.getSelectionModel().getSelectedItem())
                    || this.sportsComboBox.getSelectionModel().getSelectedItem().equals("Sport"))) {
                listDPS.add(dps);
            }
        }
        tileInitialize(listDPS);
    }

    /**
     * Adds a new DPS event to the list and updates the combo boxes if necessary.
     *
     * @param dps The DPS event to be added.
     */
    public void ajouterDpsList(DPS dps) {
        if (!this.sports.contains(dps.getSport().getNom())) {
            this.sports.add(dps.getSport().getNom());
            this.sportsComboBox.getItems().add(dps.getSport().getNom());
        }

        if (!this.sites.contains(dps.getSite().getNom())) {
            this.sites.add(dps.getSite().getNom());
            this.sitesComboBox.getItems().add(dps.getSite().getNom());
        }
    }

    /**
     * Removes a DPS event from the list and updates the combo boxes if necessary.
     *
     * @param dps The DPS event to be removed.
     */
    public void retirerDpsList(DPS dps) {
        boolean autresSportExistent = false;
        for (DPS autreDps : this.dpsManagement.getDps()) {
            if (!(autreDps.getId() == dps.getId()) && autreDps.getSport().getNom().equals(dps.getSport().getNom())) {
                autresSportExistent = true;
            }
        }

        if (!autresSportExistent) {
            if (dps.getSport().getNom().equals(this.sportsComboBox.getSelectionModel().getSelectedItem())) {
                this.sportsComboBox.getSelectionModel().select("Sport");
            }
            this.sports.remove(dps.getSport().getNom());
            this.sportsComboBox.getItems().remove(dps.getSport().getNom());
        }

        boolean autresSiteExistent = false;
        for (DPS autreDps : this.dpsManagement.getDps()) {
            if (!(autreDps.getId() == dps.getId()) && autreDps.getSite().getNom().equals(dps.getSite().getNom())) {
                autresSiteExistent = true;
            }
        }

        if (!autresSiteExistent) {
            if (dps.getSite().getNom().equals(this.sitesComboBox.getSelectionModel().getSelectedItem())) {
                this.sitesComboBox.getSelectionModel().select("Site");
            }
            this.sites.remove(dps.getSite().getNom());
            this.sitesComboBox.getItems().remove(dps.getSite().getNom());
        }
    }

    /**
     * Sets the FenetreGestionController for this controller.
     *
     * @param fenetreGestionController The FenetreGestionController to be set.
     */
    public void setFenetreGestionController(FenetreGestionController fenetreGestionController) {
        this.fenetreGestionController = fenetreGestionController;
    }

    /**
     * Sets the EvenementController for this controller.
     *
     * @param evenementController The EvenementController to be set.
     */
    public void setEvenementController(DisplayUpComingEventController evenementController) {
        this.evenementController = evenementController;
    }

    /**
     * Handles the action of opening the rescuer management window.
     */
    @FXML
    public void GestionSecouristeButton() {
        this.fenetreGestionController.loadContent2("/fxml/admin/DashboardRescuer.fxml");
    }

    /**
     * Handles the action of opening the DPS management window.
     */
    @FXML
    private void calendarAssignment() {
        this.fenetreGestionController.loadContent2("/fxml/common/DashboardCalendarAssignment.fxml");
    }

    /**
     * Handles the action of opening the map management window.
     */
    @FXML
    private void carteLink() {
        this.fenetreGestionController.loadContent2("/fxml/map/DashboardMap.fxml");
    }

    /**
     * Handles the action of opening the DPS creation popup.
     */
    @FXML
    public void CreationDPSButton() {
        StackPane overlay = showOverlay();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/PopupAddDps.fxml"));
            Parent overlayContent = loader.load();
            PopupAddDpsController controller = loader.getController();
            controller.initializeGestionEvenementController(this);
            controller.initializeEvenementController(this.evenementController);
            overlay.getChildren().add(overlayContent);
            StackPane.setAlignment(overlayContent, javafx.geometry.Pos.CENTER);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of opening the relation competence popup.
     */
    @FXML
    private void relationCompetence() {
        StackPane overlay = showOverlay();
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/PopupRelation.fxml"));
            Parent overlayContent = loader.load();
            overlay.getChildren().add(overlayContent);
            StackPane.setAlignment(overlayContent, javafx.geometry.Pos.CENTER);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of opening the DPS management window.
     */
    private void majAffect(DPS dps) {
        try {
            this.affectationManagement.launchAffectation(dps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (!this.besoinManagement.getBesoinByDPS(dps).getCompetences().isEmpty()) {
            Alert alert = PopupAddDpsController.alertBox();
            alert.showAndWait();
        }
        filtreUpdate();
    }

    /**
     * Opens a confirmation popup to delete a DPS event.
     *
     * @param dps The DPS event to be deleted.
     */
    private void supprDps(DPS dps) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/PopupDeleteDps.fxml"));
            Parent overlayContent = loader.load();

            PopupDeleteDpsController controller = loader.getController();
            controller.initializeDPS(dps, this, this.evenementController);

            StackPane overlayPane = showOverlay();

            overlayPane.getChildren().add(overlayContent);
            StackPane.setAlignment(overlayContent, Pos.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a popup to modify the assignment of a DPS event.
     *
     * @param dps The DPS event to be modified.
     */
    private void modifAffect(DPS dps) {
        StackPane overlay = showOverlay();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/PopupUpdateDps.fxml"));
            Parent overlayContent = loader.load();
            PopupUpdateDpsController controller = loader.getController();

            controller.initializeDpsUpdate(dps);
            controller.initializeGestionEvenementController(this);
            controller.initializeEvenementController(this.evenementController);
            controller.initialize();

            overlay.getChildren().add(overlayContent);
            StackPane.setAlignment(overlayContent, javafx.geometry.Pos.CENTER);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
