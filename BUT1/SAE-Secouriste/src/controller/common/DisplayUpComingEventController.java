package controller.common;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.data.persistence.Affectation;
import model.data.service.AffectationManagement;
import model.data.service.DPSManagement;
import model.data.persistence.DPS;
import model.data.service.SecouristeManagement;

import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * Controller for managing and displaying the list of events (DPS).
 * Loads events based on the current user’s rights (admin or rescuer).
 * Formats and displays each event in a styled GridPane within a VBox.
 *
 * @author C.Brocart, T.Brami-Coatual, L.Carré, G.Potay
 * @version 1.0
 */
public class DisplayUpComingEventController {

    /** VBox container where event entries are displayed */
    @FXML
    private VBox listEvent;

    /** Label displaying the count of displayed events */
    @FXML
    private Label countLabel;

    /** Service to manage DPS data */
    private final DPSManagement dpsManagement = new DPSManagement();

    /** Service to manage affectations */
    private final AffectationManagement  affectationManagement = new AffectationManagement();

    /** Service to manage rescuers (secouristes) */
    private final SecouristeManagement secouristeManagement = new SecouristeManagement();

    /**
     * Initializes the controller by loading the list of events (DPS).
     * Admin users see all events; rescuers see only their assigned events.
     * Converts the DPS list into styled GridPanes and adds them to the VBox.
     */
    @FXML
    public void initialize() {
        List<DPS> listDPS = new ArrayList<>();
        if (getInstanceAuthentificationManagement().isAdmin()) {
            listDPS = this.dpsManagement.getDps();
        } else {
            List<Affectation> affectations = this.affectationManagement.getAffectationsByRescuer(secouristeManagement.getSecouristeById(getInstanceAuthentificationManagement().getCurrentUser().getIdUser()));
            for (Affectation affectation : affectations) {
                listDPS.add(affectation.getDPSAffect());
            }
        }

        ArrayList<GridPane> list = listDPSToListGridPane(listDPS);
        listEvent.getChildren().clear();
        listEvent.getChildren().addAll(list);
        listEvent.setSpacing(10);
        listEvent.setStyle("-fx-background-color: #121215;");

        countLabel.setText(String.valueOf(listEvent.getChildren().size()));
    }

    /**
     * Converts a list of DPS events into a list of styled GridPane nodes
     * representing each event with its name, date, and a colored status circle.
     *
     * @param listDPS List of DPS events to convert
     * @return ArrayList of GridPane nodes, each representing an event
     */
    private ArrayList<GridPane> listDPSToListGridPane(List<DPS> listDPS) {
        ArrayList<GridPane> list = new ArrayList<>();
        for (int i = 0; i < listDPS.size(); i++) {
            DPS dps = listDPS.get(i);
            Label label = new Label(dps.getName());
            label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-family: 'Poppins';");

            String[] months = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" };

            Label date = new Label(dps.getJournee().getJour() + " " + months[dps.getJournee().getMois() - 1] + " de " + dps.getHoraireDepart() + "h à " + dps.getHoraireFin() + "h");
            date.setStyle("-fx-text-fill: #EDF2F66F ; " +
                    "-fx-font-family: 'Poppins'; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: 300; " +
                    "-fx-letter-spacing: -0.56px;");

            Circle circle = new Circle();
            circle.setRadius(20);
            LocalDate today = LocalDate.now();
            LocalDate eventDate = LocalDate.of(dps.getJournee().getAnnee(), dps.getJournee().getMois(), dps.getJournee().getJour());
            long daysBetween = ChronoUnit.DAYS.between(today, eventDate);
            if (daysBetween < 7) {
                circle.setFill(Color.web("#3EAD42"));
            } else {
                circle.setFill(Color.web("#4A4AE4"));
            }

            GridPane pane = new GridPane();
            GridPane subPane = new GridPane();

            subPane.setPadding(new Insets(0, 0, 0, 15));

            ColumnConstraints col = new ColumnConstraints();
            col.setMaxWidth(500);
            col.setPrefWidth(500);
            pane.getColumnConstraints().addAll(col);

            subPane.add(label, 0, 0);
            subPane.add(date, 0, 1);
            pane.add(subPane,0,0);
            pane.add(circle,1,0);
            pane.setPrefHeight(60);
            pane.setMaxHeight(60);
            pane.setPadding(new Insets(10));
            VBox.setMargin(pane, new Insets(0,10,0,0));
            pane.setStyle("-fx-background-color: rgba(249, 252, 255, 0.07); -fx-background-radius: 50");
            label.setLayoutX(10);
            label.setLayoutY(10);
            list.add(pane);
        }
        return list;
    }
}
