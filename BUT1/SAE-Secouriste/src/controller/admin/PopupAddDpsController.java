package controller.admin;

import controller.common.DisplayUpComingEventController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import model.data.persistence.*;
import model.data.service.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static controller.layoutmanager.FenetreGestionController.removeOverlay;

/**
 * Controller class for the DPS Addition Window.
 * Responsible for managing the UI and logic to create a new DPS event,
 * including input validation, competence assignment, and updating related data.
 *
 * Authors: C.Brocart, T.Brami-Coatual, L.Carré, G.Potay
 * Version: 1.0
 */
public class PopupAddDpsController {


    /**
     * The root layout pane of the DPS addition window.
     */
    @FXML
    private AnchorPane rootPane;

    /**
     * ComboBox to select the sport for the DPS.
     */
    @FXML
    private ComboBox<String> sportComboBox;

    /**
     * ComboBox to select the site for the DPS.
     */
    @FXML
    private ComboBox<String> siteComboBox;

    /**
     * ComboBox to select the start hour of the DPS.
     */
    @FXML
    private ComboBox<Integer> horaireDebComboBox;

    /**
     * ComboBox to select the end hour of the DPS.
     */
    @FXML
    private ComboBox<Integer> horaireFinComboBox;

    /**
     * ComboBox to select the day of the DPS date.
     */
    @FXML
    private ComboBox<Integer> jourComboBox;

    /**
     * ComboBox to select the month of the DPS date.
     */
    @FXML
    private ComboBox<String> moisComboBox;

    /**
     * ComboBox to select the year of the DPS date.
     */
    @FXML
    private ComboBox<Integer> anneeComboBox;

    /**
     * ComboBox to select number of required PSE1 competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxPSE1;

    /**
     * ComboBox to select number of required PSE2 competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxPSE2;

    /**
     * ComboBox to select number of required SSA competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxSSA;

    /**
     * ComboBox to select number of required CE competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxCE;

    /**
     * ComboBox to select number of required VPSP competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxVPSP;

    /**
     * ComboBox to select number of required CP competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxCP;

    /**
     * ComboBox to select number of required PBC competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxPBC;

    /**
     * ComboBox to select number of required CO competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxCO;

    /**
     * ComboBox to select number of required PBF competencies.
     */
    @FXML
    private ComboBox<Integer> comboBoxPBF;

    /**
     * TextField to enter the name of the DPS event.
     */
    @FXML
    private TextField nomTextField;

    /**
     * Label used to display information or error messages.
     */
    @FXML
    private Label infosLabel;

    /**
     * The date of the DPS event.
     */
    LocalDate date;

    /**
     * The starting hour of the DPS event.
     */
    private int horaireDeb;

    /**
     * The ending hour of the DPS event.
     */
    private int horaireFin;

    /**
     * Array of month names in French, used for month selection.
     */
    private final String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

    /**
     * The selected site for the DPS.
     */
    private Site site;

    /**
     * The selected sport for the DPS.
     */
    private Sport sport;

    /**
     * Reference to the event management controller (not injected here).
     */
    private DashboardEventController dashboardEventController;

    /**
     * Reference to the main event controller (not injected here).
     */
    private DisplayUpComingEventController evenementController;

    /**
     * Service managing sports data.
     */
    private final SportManagement sportManagement = new SportManagement();

    /**
     * Service managing sites data.
     */
    private final SiteManagement siteManagement = new SiteManagement();

    /**
     * Service managing DPS events data.
     */
    private final DPSManagement dpsManagement = new DPSManagement();

    /**
     * Service managing competency needs data.
     */
    private final BesoinManagement besoinManagement = new BesoinManagement();

    /**
     * Service managing assignments data.
     */
    private final AffectationManagement  affectationManagement = new AffectationManagement();

    /**
     * Initializes the controller after the FXML is loaded.
     * Sets default values for date, sport, site, and initializes all ComboBoxes.
     * Also configures the info label style.
     */
    @FXML
    public void initialize() {
        LocalDate today = LocalDate.now();
        this.date = today.plusDays(7);

        this.infosLabel.setWrapText(true);
        this.infosLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        this.horaireDeb = 8;
        this.horaireFin = 12;

        this.sport = this.sportManagement.getSports().get(0);
        this.site = this.siteManagement.getSites().get(0);

        initializeComboBoxCompetences();
        initializeComboBoxSport();
        initializeComboBoxSite();
        initializeComboBoxHoraire();
        initializeComboBoxDate();
    }

    /**
     * Initializes all competence ComboBoxes with integer values from 0 to 50.
     * Default selection is 0 for all.
     */
    private void initializeComboBoxCompetences() {
        this.comboBoxCE.getItems().clear();
        this.comboBoxPSE1.getItems().clear();
        this.comboBoxPSE2.getItems().clear();
        this.comboBoxSSA.getItems().clear();
        this.comboBoxVPSP.getItems().clear();
        this.comboBoxCP.getItems().clear();
        this.comboBoxPBC.getItems().clear();
        this.comboBoxCO.getItems().clear();
        this.comboBoxPBF.getItems().clear();

        for (int i = 0; i <= 50; i++) {
            this.comboBoxPSE1.getItems().add(i);
            this.comboBoxPSE2.getItems().add(i);
            this.comboBoxSSA.getItems().add(i);
            this.comboBoxCE.getItems().add(i);
            this.comboBoxVPSP.getItems().add(i);
            this.comboBoxCP.getItems().add(i);
            this.comboBoxPBC.getItems().add(i);
            this.comboBoxCO.getItems().add(i);
            this.comboBoxPBF.getItems().add(i);
        }
        this.comboBoxPSE1.getSelectionModel().select(0);
        this.comboBoxPSE2.getSelectionModel().select(0);
        this.comboBoxSSA.getSelectionModel().select(0);
        this.comboBoxCE.getSelectionModel().select(0);
        this.comboBoxVPSP.getSelectionModel().select(0);
        this.comboBoxCP.getSelectionModel().select(0);
        this.comboBoxPBC.getSelectionModel().select(0);
        this.comboBoxCO.getSelectionModel().select(0);
        this.comboBoxPBF.getSelectionModel().select(0);
    }

    /**
     * Initializes the sport ComboBox with the list of available sports.
     * Selects the current sport by default.
     */
    private void initializeComboBoxSport() {
        this.sportComboBox.getItems().clear();

        for (Sport s : this.sportManagement.getSports()) {
            this.sportComboBox.getItems().add(s.getNom());
        }

        if (this.sport != null) {
            this.sportComboBox.getSelectionModel().select(this.sport.getNom());
        }
    }

    /**
     * Initializes the site ComboBox with the list of available sites.
     * Selects the current site by default.
     */
    private void initializeComboBoxSite() {

        this.siteComboBox.getItems().clear();

        for (Site s : this.siteManagement.getSites()) {
            this.siteComboBox.getItems().add(s.getNom());
        }

        if (this.site != null) {
            this.siteComboBox.getSelectionModel().select(this.site.getNom());
        }
    }

    /**
     * Initializes the start and end hour ComboBoxes with values from 0 to 24.
     * Sets the default selection for start and end hours.
     */
    private void initializeComboBoxHoraire() {
        initializeComboBoxHoraireDeb();
        this.horaireDebComboBox.getSelectionModel().select(this.horaireDeb);
        initializeComboBoxHoraireFin();
        this.horaireFinComboBox.getSelectionModel().select(this.horaireFin);
    }

    /**
     * Initializes the start hour ComboBox with integer values from 0 to 24.
     */
    private void initializeComboBoxHoraireDeb() {
        this.horaireDebComboBox.getItems().clear();

        for (int i = 0; i <= 24; i++) {
            this.horaireDebComboBox.getItems().add(i);
        }
    }

    /**
     * Initializes the end hour ComboBox with integer values from 0 to 24.
     */
    private void initializeComboBoxHoraireFin() {
        this.horaireFinComboBox.getItems().clear();

        for (int i = 0; i <= 24; i++) {
            this.horaireFinComboBox.getItems().add(i);
        }
    }

    /**
     * Initializes the date ComboBoxes for day, month, and year.
     * Ensures days before a limit date (7 days from now) are excluded.
     */
    private void initializeComboBoxDate() {
        this.jourComboBox.getItems().clear();
        this.moisComboBox.getItems().clear();
        this.anneeComboBox.getItems().clear();

        LocalDate todayLimit = LocalDate.now().plusDays(7);

        for (int i = 1; i <= this.date.lengthOfMonth(); i++) {
            if (!(this.date.getMonthValue() == todayLimit.getMonthValue() && this.date.getYear() == todayLimit.getYear() && i < todayLimit.getDayOfMonth())) {
                this.jourComboBox.getItems().add(i);
            }
        }

        if (this.jourComboBox.getItems().contains(this.date.getDayOfMonth())) {
            this.jourComboBox.getSelectionModel().select((Integer) this.date.getDayOfMonth());
        }

        for (int i = 1; i <= 12; i++) {
            if (!(this.date.getYear() == todayLimit.getYear() && i < todayLimit.getMonthValue())) {
                this.moisComboBox.getItems().add(this.months[i - 1]);
            }
        }

        this.moisComboBox.getSelectionModel().select(this.months[this.date.getMonthValue() - 1]);

        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i <= currentYear + 6; i++) {
            this.anneeComboBox.getItems().add(i);
        }

        this.anneeComboBox.getSelectionModel().select((Integer) this.date.getYear());
    }

    /**
     * Handles updates to the filter ComboBoxes when the user changes selections.
     * Updates the internal state for sport, site, date, and hours accordingly.
     *
     * @param event the ActionEvent triggered by a ComboBox value change.
     */
    @FXML
    public void filterUpdate(ActionEvent event) {
        Object source = event.getSource();

        if (source == this.sportComboBox) {
            String selectedSport = this.sportComboBox.getSelectionModel().getSelectedItem();
            if (selectedSport != null && !selectedSport.equals(this.sport.getNom())) {
                this.sport = this.sportManagement.getSportByName(selectedSport);
            }
        } else if (source == this.siteComboBox) {
            String selectedSite = this.siteComboBox.getSelectionModel().getSelectedItem();
            if (selectedSite != null && !selectedSite.equals(this.site.getNom())) {
                this.site = this.siteManagement.getSiteByName(selectedSite);
            }
        } else if (source == this.horaireDebComboBox) {
            Integer deb = this.horaireDebComboBox.getSelectionModel().getSelectedItem();
            if (deb != null) {
                this.horaireDeb = deb;
            }
        } else if (source == this.horaireFinComboBox) {
            Integer fin = this.horaireFinComboBox.getSelectionModel().getSelectedItem();
            if (fin != null) {
                this.horaireFin = fin;
            }
        } else if (source == this.jourComboBox || source == this.moisComboBox || source == this.anneeComboBox) {
            Integer jour = this.jourComboBox.getSelectionModel().getSelectedItem();
            String moisNom = this.moisComboBox.getSelectionModel().getSelectedItem();
            Integer annee = this.anneeComboBox.getSelectionModel().getSelectedItem();

            if (jour != null && moisNom != null && annee != null) {
                int mois = 0;

                for (int i = 0; i < this.months.length; i++) {
                    if (this.months[i].equals(moisNom)) {
                        mois = i + 1;
                        break;
                    }
                }

                LocalDate newDate = LocalDate.of(annee, mois, jour);

                if (!newDate.equals(this.date)) {
                    this.date = newDate;
                    initializeComboBoxDate();
                }
            }
        }
    }

    /**
     * Attempts to create a DPS instance from the current input fields.
     * Performs validation on time range and DPS name uniqueness.
     * Updates the info label with error messages if validation fails.
     *
     * @return a new DPS object if inputs are valid, otherwise null.
     */
    public DPS ajoutDPS() {
        DPS dps = null;
        if (this.horaireDeb >= this.horaireFin) {
            this.infosLabel.setText("Erreur : Horaire de début >= Horaire de fin");
        } else if (this.horaireFin - this.horaireDeb < 3) {
            this.infosLabel.setText("Erreur : Le Dps doit durer plus de 3h");
        } else {
            long id = this.dpsManagement.numberOfDps();
            while (this.dpsManagement.exists(id)) {
                id++;
            }
            if (this.nomTextField.getText().isEmpty()) {
                this.infosLabel.setText("Erreur : Nom de DPS n'existe pas");
            } else {
                boolean dpsNameExists = false;
                for (DPS autreDps : dpsManagement.getDps()) {
                    if (autreDps.getName().equals(this.nomTextField.getText())) {
                    dpsNameExists = true;
                    }
                }
                if (!dpsNameExists) {
                    Journee journee = new Journee(this.date.getDayOfMonth(), this.date.getMonthValue(), this.date.getYear());
                    dps = new DPS(id, this.nomTextField.getText(), this.horaireDeb, this.horaireFin, this.site, this.sport, journee);
                } else {
                    this.infosLabel.setText("Erreur : Nom de DPS déjà existant");
                }
            }
        }
        return dps;
    }

    /**
     * Assigns a DPS (personnel) with selected competences.
     * The method retrieves the selected values from various combo boxes,
     * constructs a list of competences, and adds the DPS to the system if valid.
     * An alert is shown if some competences could not be assigned.
     */
    public void affectDPS() {
        DPS dps = this.ajoutDPS();
        if (dps != null) {
            ArrayList<Competence> competences = new ArrayList<>();

            for (int i = 0; i < this.comboBoxCE.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("CE"));
            }

            for (int i = 0; i < this.comboBoxCO.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("CO"));
            }

            for (int i = 0; i < this.comboBoxCP.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("CP"));
            }

            for (int i = 0; i < this.comboBoxPSE1.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("PSE1"));
            }

            for (int i = 0; i < this.comboBoxPSE2.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("PSE2"));
            }

            for (int i = 0; i < this.comboBoxPBC.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("PBC"));
            }

            for (int i = 0; i < this.comboBoxSSA.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("SSA"));
            }

            for (int i = 0; i < this.comboBoxPBF.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("PBF"));
            }

            for (int i = 0; i < this.comboBoxVPSP.getSelectionModel().getSelectedItem(); i++) {
                competences.add(new Competence("VPSP"));
            }

            if (competences.isEmpty()) {
                this.infosLabel.setText("Erreur : Aucune compétences renseignées");
            } else {
                this.dpsManagement.addDps(dps);
                Alert alert = alertBox();
                try {
                    this.besoinManagement.addBesoin(new Besoin(dps, competences));
                    this.affectationManagement.launchAffectation(dps);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if (!this.besoinManagement.getBesoinByDPS(dps).getCompetences().isEmpty()) {
                    alert.showAndWait();
                }
                this.dashboardEventController.ajouterDpsList(dps);
                this.dashboardEventController.filtreUpdate();
                this.evenementController.initialize();
                annuleDPS();
            }
        }
    }

    /**
     * Creates and configures an information alert to notify the user
     * when some competences were not assigned.
     *
     * @return a styled Alert object
     */
    static Alert alertBox() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Affectation partielle");
        alert.setHeaderText("Certaines compétences n'ont pas été affectées");
        alert.setContentText("Il sera possible de mettre à jour l'affectation plus tard.");

        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getScene().setFill(Color.TRANSPARENT);

        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle(
                    "-fx-font-size: 18px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: #2C3E50; " +
                            "-fx-padding: 20 0 10 0;"
            );
        }

        Label contentLabel = (Label) dialogPane.lookup(".content");
        if (contentLabel != null) {
            contentLabel.setStyle(
                    "-fx-font-size: 14px; " +
                            "-fx-text-fill: #34495E; " +
                            "-fx-padding: 10 20 20 20; " +
                            "-fx-line-spacing: 2px;"
            );
        }

        dialogPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 35; -fx-border-width: 3; border-radius: 35; -fx-border-color: #000000");

        dialogPane.getStylesheets().add("data:text/css,.dialog-pane {-fx-background-radius: 20; -fx-border-radius: 20; } " +
                ".dialog-pane .header-panel {-fx-background-radius: 20 20 0 0; } " +
                ".dialog-pane .button-bar {-fx-background-radius: 0 0 20 20; }");

        dialogPane.lookupButton(ButtonType.OK).setStyle("-fx-background-color: #0C0D0F; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius : 15;");
        return alert;
    }

    /**
     * Cancels the DPS creation process and removes any overlay from the UI.
     */
    public void annuleDPS() {
        removeOverlay();
        rootPane.getChildren().removeAll();
    }

    /**
     * Initializes the controller used to manage event-related DPS actions.
     *
     * @param dashboardEventController the controller managing event operations
     */
    public void initializeGestionEvenementController(DashboardEventController dashboardEventController) {
        this.dashboardEventController = dashboardEventController;
    }

    /**
     * Initializes the controller responsible for general event handling.
     *
     * @param evenementController the main event controller
     */
    public void initializeEvenementController(DisplayUpComingEventController evenementController) {
        this.evenementController = evenementController;
    }
}