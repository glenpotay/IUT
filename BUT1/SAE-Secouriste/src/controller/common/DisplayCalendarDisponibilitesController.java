package controller.common;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.data.persistence.Disponibilite;
import model.data.persistence.Journee;
import model.data.service.SecouristeManagement;
import model.data.service.DisponibiliteManagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;

/**
 * DisplayCalendarDisponibilitesController is responsible for managing the calendar view
 * where users can set their availability for specific dates.
 * It allows users to select a range of dates and save their availability.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DisplayCalendarDisponibilitesController {

    /**
     * The AnchorPane that serves as the calendar view.
     */
    @FXML
    private GridPane calendarGrid;

    /**
     * The Button to navigate to the next month.
     */
    @FXML
    private AnchorPane settingsPane;

    /**
     * The Button to navigate to the previous month.
     */
    private final DisponibiliteManagement disponibiliteManagement = new DisponibiliteManagement();

    /**
     * The Secouriste for whom the availability is being managed.
     */
    private model.data.persistence.Secouriste sec;

    /**
     * The month and year currently displayed in the calendar.
     */
    private int mois;

    /**
     * The year currently displayed in the calendar.
     */
    private int annee;

    /**
     * A set of LocalDate objects representing the available dates.
     */
    private final Set<LocalDate> disponibilites = new HashSet<>();

    /**
     * A map that associates each Label with its corresponding LocalDate.
     */
    private final Map<Label, LocalDate> labelDateMap = new HashMap<>();

    /**
     * A list of Labels that are currently selected.
     */
    private final List<Label> selectedLabels = new ArrayList<>();

    /**
     * A temporary set of LocalDate objects used for selection.
     */
    private final Set<LocalDate> tempSelection = new HashSet<>();

    /**
     * The start date of the current selection.
     */
    private LocalDate selectionStart = null;

    /**
     * The end date of the current selection.
     */
    private LocalDate selectionEnd = null;

    /**
     * The Pane that contains the calendar assignment view.
     */
    @FXML private Pane calendarAssignmentPane;

    /**
     * The Label that displays the current month and year.
     */
    @FXML
    private Label jourLabel;

    /**
     * Initializes the DisplayCalendarDisponibilitesController by setting up the calendar view.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    private void initialize() {
        LocalDate today = LocalDate.now();
        this.mois = today.getMonthValue();
        this.annee = today.getYear();
        try {
            sec = new SecouristeManagement().getSecouristeById(model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement().getCurrentUser().getIdUser());
            chargerDisponibilites();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        populateCalendar(this.annee, this.mois);
    }

    /**
     * Handles the button click event to navigate to the next month.
     * If the current month is December, it moves to January of the next year.
     */
    private void chargerDisponibilites() {
        disponibilites.clear();
        ArrayList<Disponibilite> dispoList = disponibiliteManagement.getDisponibilites(sec);
        for (Disponibilite dispo : dispoList) {
            Journee journee = dispo.getJourDisp();
            LocalDate date = LocalDate.of(journee.getAnnee(), journee.getMois(), journee.getJour());
            disponibilites.add(date);
        }
    }

    /**
     * Handles the button click event to save the selected availability.
     * It updates the availability in the database and provides feedback to the user.
     */
    private void sauvegarderDisponibilites() {
        long idSec = getInstanceAuthentificationManagement().getCurrentUser().getIdUser();

        // Récupérer les disponibilités actuelles en base
        ArrayList<Disponibilite> dispoCourantes = disponibiliteManagement.getDisponibilites(sec);
        Set<LocalDate> datesCourantes = new HashSet<>();

        for (Disponibilite dispo : dispoCourantes) {
            Journee journee = dispo.getJourDisp();
            LocalDate date = LocalDate.of(journee.getAnnee(), journee.getMois(), journee.getJour());
            datesCourantes.add(date);
        }

        // Supprimer les disponibilités qui ne sont plus sélectionnées
        for (LocalDate date : datesCourantes) {
            if (!disponibilites.contains(date)) {
                Journee journee = new Journee(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                disponibiliteManagement.removeDisponibilite(sec, journee);
            }
        }

        // Ajouter les nouvelles disponibilités
        for (LocalDate date : disponibilites) {
            if (!datesCourantes.contains(date)) {
                Journee journee = new Journee(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                disponibiliteManagement.addDisponibilite(sec, journee);
            }
        }

        System.out.println("Disponibilités sauvegardées : " + disponibilites.size() + " jours");
    }

    /**
     * Handles the button click event to navigate to the next month.
     * If the current month is December, it moves to January of the next year.
     */
    @FXML
    private void moisSuivant() {
        if (this.mois == 12) {
            this.annee++;
            this.mois = 1;
            calendarGrid.getChildren().clear();
            populateCalendar(this.annee, this.mois);
        } else {
            calendarGrid.getChildren().clear();
            this.mois++;
            populateCalendar(this.annee, this.mois);
        }
    }

    /**
     * Handles the button click event to navigate to the previous month.
     * If the current month is January, it moves to December of the previous year.
     */
    @FXML
    private void moisPrecedent() {
        if (this.mois == 1) {
            calendarGrid.getChildren().clear();
            this.mois = 12;
            this.annee--;
            populateCalendar(this.annee, this.mois);
        } else {
            calendarGrid.getChildren().clear();
            this.mois--;
            populateCalendar(this.annee, this.mois);
        }
    }

    /**
     * Handles the button click event to navigate to the calendar assignment view.
     * It loads the calendar assignment view and displays it in the calendar assignment pane.
     */
    private void populateCalendar(int annee, int mois) {
        calendarGrid.getChildren().clear();
        labelDateMap.clear();
        selectedLabels.clear();
        tempSelection.clear();
        String[] months = {
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };
        jourLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 24px");
        jourLabel.setText(months[mois - 1] + " " + annee);
        String[] days = { "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim" };
        for (int i = 0; i < days.length; i++) {
            Label dayHeaderLabel = new Label(days[i]);
            dayHeaderLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 16px");
            calendarGrid.add(dayHeaderLabel, i, 0);
        }
        java.time.YearMonth yearMonth = java.time.YearMonth.of(annee, mois);
        LocalDate firstDay = yearMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue();
        int col = dayOfWeek - 1;
        int row = 1;
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(annee, mois, day);
            Label dayLabel = new Label(String.valueOf(day));
            applyDayStyle(dayLabel, date);
            dayLabel.setMinSize(40, 40);
            dayLabel.setMaxSize(40, 40);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setFocusTraversable(true);
            dayLabel.setPickOnBounds(true);
            labelDateMap.put(dayLabel, date);
            setupMouseHandlers(dayLabel);
            calendarGrid.add(dayLabel, col, row);
            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
        for (Node node : calendarGrid.getChildren()) {
            if (node instanceof Label label) {
                LocalDate date = labelDateMap.get(label);
                if (date != null && date.equals(LocalDate.now())) {
                    calendarGrid.getChildren().remove(label);
                    calendarGrid.getChildren().add(label);
                    break;
                }
            }
        }
        calendarGrid.setOnMouseDragged(e -> {
            Node node = e.getPickResult().getIntersectedNode();
            while (node != null && !(node instanceof Label)) {
                node = node.getParent();
            }
            if (node instanceof Label label && labelDateMap.containsKey(label)) {
                selectionEnd = labelDateMap.get(label);
                updateSelectionVisuals();
            }
        });
    }

    /**
     * Applies the appropriate style to a day label based on its date.
     * It highlights the current day and available dates with different styles.
     *
     * @param dayLabel The label representing the day.
     * @param date     The LocalDate associated with the label.
     */
    private void applyDayStyle(Label dayLabel, LocalDate date) {
        LocalDate today = LocalDate.now();
        boolean isToday = date.equals(today);
        boolean isDisponible = disponibilites.contains(date);
        if (isToday && isDisponible) {
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 5;-fx-background-insets: 1; -fx-background-color: #262673; -fx-font-size: 16px");
        } else if (isToday) {
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 5;-fx-background-insets: 1; -fx-background-color: #4A4AE4; -fx-font-size: 16px");
        } else if (isDisponible) {
            dayLabel.setStyle("-fx-padding: 0;-fx-background-insets: 1; -fx-background-color: #7272ff; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 16px");
        } else {
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 16px");
        }
    }

    /**
     * Sets up mouse handlers for the day label to handle selection and deselection.
     * It allows users to select a range of dates by clicking and dragging.
     *
     * @param dayLabel The label representing the day.
     */
    private void setupMouseHandlers(Label dayLabel) {
        dayLabel.setOnMousePressed(e -> {
            LocalDate date = labelDateMap.get(dayLabel);
            if (date != null) {
                selectionStart = date;
                selectionEnd = date;
                updateSelectionVisuals();
            }
        });
        dayLabel.setOnMouseReleased(e -> {
            if (selectionStart != null && selectionEnd != null) {
                LocalDate start = selectionStart.isBefore(selectionEnd) ? selectionStart : selectionEnd;
                LocalDate end = selectionStart.isAfter(selectionEnd) ? selectionStart : selectionEnd;
                traiterSelection(start, end);
                clearTempSelection();
            }
        });
    }

    /**
     * Handles the selection of a range of dates.
     * It checks if the selected period is available and updates the availability accordingly.
     *
     * @param start The start date of the selection.
     * @param end   The end date of the selection.
     */
    private void traiterSelection(LocalDate start, LocalDate end) {
        Set<LocalDate> periodeSelectionnee = new HashSet<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            periodeSelectionnee.add(current);
            current = current.plusDays(1);
        }
        boolean toutDisponible = disponibilites.containsAll(periodeSelectionnee);
        if (toutDisponible) {
            disponibilites.removeAll(periodeSelectionnee);
            System.out.println("Période retirée : " + start + " -> " + end);
        } else {
            disponibilites.addAll(periodeSelectionnee);
            System.out.println("Période ajoutée : " + start + " -> " + end);
        }
        updateCalendarDisplay();
        sauvegarderDisponibilites();
    }

    /**
     * Updates the visual representation of the selected dates in the calendar.
     * It highlights the selected dates and applies styles to them.
     */
    private void updateSelectionVisuals() {
        updateCalendarDisplay();
        if (selectionStart == null || selectionEnd == null) return;
        LocalDate start = selectionStart.isBefore(selectionEnd) ? selectionStart : selectionEnd;
        LocalDate end = selectionStart.isAfter(selectionEnd) ? selectionStart : selectionEnd;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            tempSelection.add(current);
            current = current.plusDays(1);
        }
        for (Map.Entry<Label, LocalDate> entry : labelDateMap.entrySet()) {
            LocalDate date = entry.getValue();
            if (tempSelection.contains(date)) {
                Label l = entry.getKey();
                LocalDate today = LocalDate.now();
                boolean isToday = date.equals(today);
                if (isToday) {
                    l.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 5; -fx-background-color: #777777; -fx-font-size: 16px");
                } else {
                    l.setStyle("-fx-padding: 0; -fx-background-color: #777777; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 16px");
                }
                selectedLabels.add(l);
            }
        }
    }

    /**
     * Updates the calendar display to reflect the current availability.
     * It applies styles to the labels based on their availability status.
     */
    private void updateCalendarDisplay() {
        for (Map.Entry<Label, LocalDate> entry : labelDateMap.entrySet()) {
            Label label = entry.getKey();
            LocalDate date = entry.getValue();
            applyDayStyle(label, date);
        }
    }

    /**
     * Clears the temporary selection and resets the selection start and end dates.
     * It also updates the calendar display to reflect the cleared selection.
     */
    private void clearTempSelection() {
        tempSelection.clear();
        selectedLabels.clear();
        selectionStart = null;
        selectionEnd = null;
        updateCalendarDisplay();
    }
}
