package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static model.utils.FetchDatabaseCredentials.*;

/**
 * Class ConnectionBDD that manages the connection to the database.
 * It uses credentials fetched from a utility class to establish the connection.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class ConnectionBDD {

    /**
     * Static variables for database connection parameters.
     */
    private static final String username = getUsername();

    /**
     * Static variable for database password.
     */
    private static final String password = getPassword();

    /**
     * Static variable for database URL.
     */
    private static final String url = getUrl();

    /**
     * Static variable for the database connection.
     */
    private static Connection connexion;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    public static Connection getConnection() throws SQLException {
        // System.out.println("Connexion en cours");
        if (connexion == null || connexion.isClosed()) {

            connexion = DriverManager.getConnection( url, username, password);
            // System.out.println(url + " " + username);
            // System.out.println("Connexion réussie");
        }
        return connexion;
    }
}