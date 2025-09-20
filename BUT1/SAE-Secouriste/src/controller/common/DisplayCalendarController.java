package controller.common;
import java.time.LocalDate;
import java.time.YearMonth;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * DisplayCalendarController is responsible for managing the calendar view in the application.
 * It allows users to navigate through months and displays the days of the selected month.
 * The current day is highlighted in the calendar.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DisplayCalendarController {

    /**
     * The GridPane that serves as the calendar layout.
     */
    @FXML
    private GridPane calendarGrid;

    /**
     * The Label that displays the current month and year.
     */
    @FXML
    private Label jourLabel;

    /**
     * The month and year currently displayed in the calendar.
     */
    private int mois;

    /**
     * The year currently displayed in the calendar.
     */
    private int annee;

    /**
     * Initializes the DisplayCalendarController by setting up the calendar view.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        LocalDate today = LocalDate.now();
        this.mois = today.getMonthValue();
        this.annee = today.getYear();
        populateCalendar(this.annee, mois);
    }

    /**
     * Handles the button click event to navigate to the next month.
     * If the current month is December, it moves to January of the next year.
     */
    @FXML
    private void moisSuivant() {
        LocalDate today = LocalDate.now();
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
        LocalDate today = LocalDate.now();
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
     * Populates the calendar grid with the days of the specified month and year.
     * It highlights the current day if it falls within the displayed month and year.
     *
     * @param year  The year to display in the calendar.
     * @param month The month to display in the calendar (1 = January, ..., 12 = December).
     */
    public void populateCalendar(int year, int month) {

        String[] months = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" };
        jourLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 24px");
        jourLabel.setText(months[this.mois - 1] + " " + this.annee);

        // Jours de la semaine, en commençant par lundi
        String[] days = { "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim" };

        // Ajout des noms des jours en première ligne (row 0)
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 16px");
            calendarGrid.add(dayLabel, i, 0);
        }

        YearMonth yearMonth = YearMonth.of(year, month);

        // Le jour de la semaine du 1er du mois (1 = Lundi, ... 7 = Dimanche)
        int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();

        int daysInMonth = yearMonth.lengthOfMonth();

        // Calcul de la colonne de départ (0-based) pour le 1er jour
        int col = firstDayOfWeek - 1;
        int row = 1;  // On commence à la ligne 1, sous les noms des jours

        // Ajout des jours du mois dans la grille
        for (int day = 1; day <= daysInMonth; day++) {
            Label dayLabel = new Label(String.valueOf(day));
            if (day == LocalDate.now().getDayOfMonth() && this.mois == LocalDate.now().getMonthValue() && this.annee == LocalDate.now().getYear()) {
                dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 100; -fx-background-color: #4A4AE4; -fx-font-size: 16px");
            } else {
                dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 16px");
            }
            dayLabel.setMinSize(40, 40);
            dayLabel.setMaxSize(40, 40);
            dayLabel.setAlignment(Pos.CENTER);
            calendarGrid.add(dayLabel, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }
}

