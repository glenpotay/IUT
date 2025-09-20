package controller.admin;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.data.persistence.*;
import model.data.service.*;
import model.graph.utils.GraphUtils;
import model.graph.utils.MatrixUtils;

import static controller.layoutmanager.FenetreGestionController.removeOverlay;

/**
 * Controller class for the DPS Addition Window.
 * Responsible for managing the UI and logic to create a new DPS event,
 * including input validation, competence assignment, and updating related data.
 *
 * Authors: C.Brocart, T.Brami-Coatual, L.Carré, G.Potay
 * Version: 1.0
 */
public class PopupRelationController {


    /**
     * The root layout pane of the DPS addition window.
     */
    @FXML
    private AnchorPane rootPane;

    /**
     * Label used to display information or error messages.
     */
    @FXML
    private Label infosLabel;

    /**
     * ComboBox to select the first competencies.
     */
    @FXML
    private ComboBox<String> comboBoxComp1;

    /**
     * ComboBox to select the second competencies.
     */
    @FXML
    private ComboBox<String> comboBoxComp2;

    /**
     * Button to confirm the addition of a new DPS.
     */
    @FXML
    private VBox vBox;

    /**
     * Service managing necessite data.
     */
    private final NecessiteManagement necessiteManagement = new NecessiteManagement();

    /**
     * Service managing competencies data.
     */
    private final CompetenceManagement competenceManagement = new CompetenceManagement();

    /**
     * Initializes the controller after the FXML is loaded.
     * Sets default values for date, sport, site, and initializes all ComboBoxes.
     * Also configures the info label style.
     */
    @FXML
    public void initialize() {
        this.infosLabel.setWrapText(true);
        this.infosLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        initializeComboBoxCompetences();
        initializeVBox();
    }

    /**
     * Handles the action when the "Ajouter" button is clicked.
     * Validates the input and adds a new necessite relation if valid.
     */
    private void initializeVBox() {
        vBox.getChildren().clear();

        for (Necessite necessite : this.necessiteManagement.getNecessites()) {
            GridPane gridPane = new GridPane();
            Label necessiteLabel = new Label("necessite");
            necessiteLabel.setStyle("-fx-font-size: 16");
            Label competence1Label = new Label(necessite.getComp1().getIntitule());
            competence1Label.setStyle("-fx-font-size: 16");
            Label competence2Label = new Label(necessite.getComp2().getIntitule());
            competence2Label.setStyle("-fx-font-size: 16");
            Button suppButton = new Button("Suppression");
            suppButton.setStyle("-fx-font-size: 16; -fx-background-color: #EBF0F6; -fx-background-radius: 10");

            suppButton.setOnAction((e) -> {
                this.necessiteManagement.removeNecessite(necessite);
                initializeVBox();
            });

            gridPane.add(competence1Label, 0, 0);
            gridPane.add(necessiteLabel, 1, 0);
            gridPane.add(competence2Label, 2, 0);
            gridPane.add(suppButton, 3, 0);

            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(15);
            vBox.getChildren().add(gridPane);
        }
    }

    /**
     * Initializes all competence ComboBoxes with integer values from 0 to 50.
     * Default selection is 0 for all.
     */
    private void initializeComboBoxCompetences() {
        this.comboBoxComp1.getItems().clear();
        this.comboBoxComp2.getItems().clear();

        for (String competence : this.competenceManagement.getCompetences()) {
            this.comboBoxComp1.getItems().add(competence);
            this.comboBoxComp2.getItems().add(competence);
        }

        this.comboBoxComp1.getSelectionModel().select(0);
        this.comboBoxComp2.getSelectionModel().select(0);
    }

    /**
     * Handles the action when the "Ajouter" button is clicked.
     */
    public void ajoutRelation() {
        Necessite newNecessite = new Necessite(new Competence(comboBoxComp1.getSelectionModel().getSelectedItem()), new Competence(comboBoxComp2.getSelectionModel().getSelectedItem()));
        if (this.necessiteManagement.isCreate(newNecessite)) {
            this.infosLabel.setText("Relation déjà existante");
        } else {
            this.necessiteManagement.addNecessite(newNecessite);
            GraphUtils graphUtils = new GraphUtils();
            MatrixUtils matrixUtils = new MatrixUtils();
            if (!graphUtils.isDAG(matrixUtils.createAdjacencyMatrix( matrixUtils.createSkillDependencyMap(this.competenceManagement.getCompetences(), this.necessiteManagement.getNecessites())))) {
                this.necessiteManagement.removeNecessite(newNecessite);
                this.infosLabel.setText("Vous essayez de créer un cycle");
            } else {
                this.infosLabel.setText("Relation ajoutée avec succès");
            }
        }
        initializeVBox();
    }

    /**
     * Cancels the DPS creation process and removes any overlay from the UI.
     */
    public void sortirRelation() {
        removeOverlay();
        rootPane.getChildren().removeAll();
    }
}