package model.dao;

/**
 * DAOFactory class that provides singleton instances of various DAO classes.
 * This class is responsible for creating and managing the instances of DAOs used in the application.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class DAOFactory {

    /**
     * Static variables for DAO instances.
     */
    private static SecouristeDAO secouristeDAO;

    /**
     * Static variable for AdministrateurDAO instance.
     */
    private static AdministrateurDAO administrateurDAO;

    /**
     * Static variables for other DAO instances.
     */
    private static DPSDAO dpsDAO;

    /**
     * Static variable for JourneeDAO instance.
     */
    private static JourneeDAO journeeDAO;

    /**
     * Static variable for UserDAO instance.
     */
    private static UserDAO userDAO;

    /**
     * Static variable for NecessiteDAO instance.
     */
    public static NecessiteDAO NecessiteDAO;

    /**
     * Returns a singleton instance of SecouristeDAO.
     * This method ensures that only one instance of SecouristeDAO is created and used throughout the application.
     *
     * @return SecouristeDAO instance
     */
    public static SecouristeDAO getSecouristeDAO() {
        if (secouristeDAO == null) {
            secouristeDAO = new SecouristeDAO();
        }
        return secouristeDAO;
    }


    public static AdministrateurDAO getAdministrateurDAO() {
        if (administrateurDAO == null) {
            administrateurDAO = new AdministrateurDAO();
        }
        return administrateurDAO;
    }


    /**
     * Returns a singleton instance of SecouristeDAO.
     * This method ensures that only one instance of SecouristeDAO is created and used throughout the application.
     *
     * @return SecouristeDAO instance
     */
    public static DPSDAO getDPSDAO() {
        if (dpsDAO == null) {
            dpsDAO = new DPSDAO();
        }
        return dpsDAO;
    }

    /**
     * Returns a singleton instance of JourneeDAO.
     * This method ensures that only one instance of JourneeDAO is created and used throughout the application.
     *
     * @return JourneeDAO instance
     */
    public static JourneeDAO getJourneeDAO() {
        if (journeeDAO == null) {
            journeeDAO = new JourneeDAO();
        }
        return journeeDAO;
    }

    /**
     * Returns a singleton instance of UserDAO.
     * This method ensures that only one instance of UserDAO is created and used throughout the application.
     *
     * @return UserDAO instance
     */
    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    /**
     *
     *
     * @return
     */
    public static NecessiteDAO getNecessiteDAO() {
        if (NecessiteDAO == null) {
            NecessiteDAO = new NecessiteDAO();
        }
        return NecessiteDAO;
    }
}