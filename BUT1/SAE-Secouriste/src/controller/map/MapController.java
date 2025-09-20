package controller.map;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static model.utils.FetchDatabaseCredentials.*;

/**
 * MapController is responsible for managing the map view in the application.
 * It loads a local HTML file containing a map and injects markers based on locations fetched from a database.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class MapController {

    /**
     * The WebView that displays the map.
     * This WebView will load a local HTML file containing the map.
     */
    @FXML
    private WebView mapWebView;

    /**
     * The WebEngine that allows interaction with the WebView.
     * It is used to execute JavaScript and manipulate the map.
     */
    private WebEngine webEngine;

    /**
     * Initializes the MapController by setting up the WebView and loading the map.
     * This method is called automatically when the FXML file is loaded.
     */
    public void initialize() {
        webEngine = mapWebView.getEngine();
        // Load your local map.html file from resources
        String url = getClass().getResource("/html/map.html").toExternalForm();
        webEngine.load(url);

        // When page finishes loading, inject markers
        webEngine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                loadLocationsAndAddMarkers();
            }
        });
    }

    /**
     * Loads locations from the database and adds markers to the map.
     * It fetches locations, converts them to a JSON string, and calls a JavaScript function to add markers.
     */
    private void loadLocationsAndAddMarkers() {
        ArrayList<Location> locations = fetchLocationsFromDB();

        // Convert list to JSON string (simple manual way)
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            json.append("{\"lat\":").append(loc.lat)
                    .append(",\"lng\":").append(loc.lng)
                    .append(",\"name\":\"").append(loc.name.replace("\"", "\\\"")).append("\"}");
            if (i < locations.size() - 1) json.append(",");
        }
        json.append("]");

        // Inject locations into JS function addMarkers
        webEngine.executeScript("addMarkers(" + json.toString() + ");");
        // webEngine.executeScript("focusOnByName('Eiffel Tower')"); // Example to focus on a specific marker
    }

    /**
     * Fetches locations from the database and returns them as an ArrayList of Location objects.
     * Each location contains latitude, longitude, and name.
     *
     * @return ArrayList of Location objects fetched from the database.
     */
    private ArrayList<Location> fetchLocationsFromDB() {
        ArrayList<Location> locations = new ArrayList<>();

        String url = getUrl();
        String user = getUsername();
        String password = getPassword();

        String query = "SELECT name, latitude, longitude FROM site, dps WHERE site.code = dps.site";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            Set<String> seenCoordinates = new HashSet<>();
            Random rand = new Random();

            while (rs.next()) {
                String name = rs.getString("name");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");

                String coordKey = latitude + "," + longitude;

                // laisser un peu d'espace entre les coordonnées pour éviter la superposition
                while (seenCoordinates.contains(coordKey)) {
                    latitude += (rand.nextDouble() - 0.5) * 0.001;
                    longitude += (rand.nextDouble() - 0.5) * 0.001;
                    coordKey = latitude + "," + longitude;
                }

                seenCoordinates.add(coordKey);
                locations.add(new Location(latitude, longitude, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

    /**
     * Represents a location with latitude, longitude, and name.
     * This class is used to store the data fetched from the database for each marker on the map.
     */
    private static class Location {
        double lat, lng;
        String name;
        Location(double lat, double lng, String name) {
            this.lat = lat;
            this.lng = lng;
            this.name = name;
        }
    }
}
