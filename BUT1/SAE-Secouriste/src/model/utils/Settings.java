package model.utils;


import java.util.prefs.Preferences;

/**
 * Settings - This class provides methods to manage application settings using Java Preferences API.
 * It allows to get and set the "useGreedy" setting, which determines whether a greedy algorithm should be used.
 *
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class Settings {

    /**
     * Preferences instance to store and retrieve application settings.
     * The settings are stored in the user root node under the class name.
     */
    private static final Preferences prefs = Preferences.userRoot().node(Settings.class.getName());

    /**
     * Key for the "useGreedy" setting in the preferences.
     * This setting determines whether a greedy algorithm should be used.
     */
    private static final String USE_GREEDY_KEY = "useGreedy";

    /**
     * Default value for the "useGreedy" setting.
     * If the setting is not found in preferences, this value will be used.
     */
    private static final boolean DEFAULT_USE_GREEDY = false;

    /**
     * Retrieves the current value of the "useGreedy" setting.
     *
     * @return true if the greedy algorithm should be used, false otherwise.
     */
    public static boolean useGreedy() {
        return prefs.getBoolean(USE_GREEDY_KEY, DEFAULT_USE_GREEDY);
    }

    /**
     * Sets the value of the "useGreedy" setting.
     *
     * @param useGreedy true to enable the greedy algorithm, false to disable it.
     */
    public static void setUseGreedy(boolean useGreedy) {
        prefs.putBoolean(USE_GREEDY_KEY, useGreedy);
    }

    /*
    public static void main(String[] args) {
        // Example usage
        System.out.println("Use Greedy: " + useGreedy());
        setUseGreedy(false);
        System.out.println("Use Greedy after setting to false: " + useGreedy());
    }
    */
}
