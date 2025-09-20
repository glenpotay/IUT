package model.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * FetchDatabaseCredentials - This class is responsible for loading database credentials
 * from a properties file named "database.properties".
 * It provides methods to retrieve the database URL, username, and password.
 *
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class FetchDatabaseCredentials {

    /**
     * Properties object to hold database credentials loaded from the properties file.
     */
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = FetchDatabaseCredentials.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find database.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading database credentials", ex);
        }
    }

    /**
     * Gets the database URL from the properties file.
     * @return the database URL as a String
     */
    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    /**
     * Gets the database username from the properties file.
     * @return the database username as a String
     */
    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    /**
     * Gets the database password from the properties file.
     * @return the database password as a String
     */
    public static String getPassword() {
        return properties.getProperty("db.password");
    }
}
