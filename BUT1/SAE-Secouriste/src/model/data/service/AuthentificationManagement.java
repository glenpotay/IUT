package model.data.service;

import model.dao.DAOFactory;
import model.dao.SecouristeDAO;
import model.dao.UserDAO;
import model.data.persistence.Administrateur;
import model.data.persistence.Secouriste;
import model.data.persistence.User;
import java.sql.SQLException;
import java.util.Objects;

import static model.dao.DAOFactory.getAdministrateurDAO;
import static model.dao.DAOFactory.getSecouristeDAO;
import static model.utils.PasswordHashing.hashPassword;
import static model.utils.PasswordHashing.verifyPassword;

/**
 * AuthentificationManagement class
 * This class manages the authentication of users, including registration, login, password recovery, and rescuer creation.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class AuthentificationManagement {

    /**
     * instance of Authentication
     */
    private static final AuthentificationManagement instance = new AuthentificationManagement();
    
    /**
     * This class manages the authentication of users.
     */
    private UserDAO userDAO;

    /**
     * Code for password recovery.
     */
    private long code;

    /**
     * User object representing the currently authenticated user.
     */
    private User user;

    /**
     * User object representing the currently authenticated user.
     */
    private Secouriste secouriste;

    /**
     * Constructor for AuthentificationManagement
     * Initializes the UserDAO to interact with user data.
     */
    public AuthentificationManagement() {
        this.userDAO = DAOFactory.getUserDAO();
    }

    /**
     * Returns the singleton instance of AuthentificationManagement.
     * @return the instance of AuthentificationManagement
     */
    public static AuthentificationManagement getInstanceAuthentificationManagement() { return instance; }

    /**
     * Registers a new user with the provided login and password.
     * The username must not be empty and must not already exist in the database.
     *
     * @param mail The username of the new user.
     * @param newPassword  The password for the new user.
     * @param newPasswordConfirmation Confirmation of the new password.
     * @return boolean indicating success or failure of the login.
     */
    public boolean register(String mail, String newPassword, String newPasswordConfirmation) throws SQLException {
        boolean didRegistrationWorked = false;
        System.out.println("Registration attempt for login: " + mail);
        if (mail != null && !mail.isEmpty() && !userDAO.doesLoginExist(mail) ) {
            if (newPassword != null && !newPassword.isEmpty() && newPassword.equals(newPasswordConfirmation)) {
                User user = new User(-1, mail, hashPassword(newPassword), "rescuer");
                userDAO.addUser(user);
                this.user = userDAO.getUserByLogin(mail);
                System.out.println("User registered successfully.");
                didRegistrationWorked = true;
            }
        }
        return didRegistrationWorked;
    }

    /**
     * Checks if the current user is an administrator.
     *
     * @return true if the current user is an administrator, false otherwise.
     */
    public boolean isAdmin() {
        if (this.user == null) {
            return false;
        }
        return "administrator".equals(this.user.getRole());
    }

    /**
     * Enum representing the possible results of a login attempt.
     */
    public enum LoginResult {
        SUCCESS,
        INVALID_LOGIN,
        INVALID_PASSWORD,
        INVALID_RESCUER,
        ERROR
    }

    /**
     * Authenticates a user based on their login and password.
     *
     * @param mail    The login of the user.
     * @param password The password of the user.
     * @return LoginResult indicating the result of the login attempt.
     */
    public LoginResult login(String mail, String password) {
        // Toujours réinitialiser l'état avant une tentative de connexion
        this.user = null;
        this.secouriste = null;

        try {
            User user = userDAO.getUserByLogin(mail);
            if (user == null) {
                return LoginResult.INVALID_LOGIN;
            }
            if (!verifyPassword(password, user.getPassword())) {
                return LoginResult.INVALID_PASSWORD;
            }
            this.user = user; // Affecte l'utilisateur courant avant de vérifier le secouriste
            if( "administrator".equals(user.getRole())) {
                return LoginResult.SUCCESS;
            } else {
                if (SecouristeIsCreated()) {
                    this.secouriste = getSecouristeDAO().findById(this.user.getIdUser());
                    return LoginResult.SUCCESS;
                } else {
                    this.user = null;
                    this.secouriste = null;
                    return LoginResult.INVALID_RESCUER;
                }
            }
        } catch (Exception e) {
            this.user = null;
            this.secouriste = null;
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
            return LoginResult.ERROR;
        }
    }


    /**
     * Receives a code for password recovery based on the user's login.
     * The code is generated as a random number based on the current time.
     *
     * @param login The login of the user requesting the code.
     */
    public boolean ReceiveCode(String login) {
        if (userDAO.doesLoginExist(login)) {
            this.code = Math.abs(System.currentTimeMillis() % 1000000);
            this.user = userDAO.getUserByLogin(login);
            System.out.println("Code : " + this.code);
            return true;
        } else {
            System.out.println("User not found.");
            this.user = null;
            return false;
        }
    }

    /**
     * Changes the user's password if the provided code matches the stored code.
     *
     * @param code  The code to verify.
     * @param npw1  The new password.
     */
    public boolean changePassword(long code, String npw1) {
        System.out.print(this.code);
        if (code == this.code) {
            if (npw1 != null && !Objects.equals(npw1, "")) {
                String hashedPassword = hashPassword(npw1); // Hash du nouveau mot de passe
                userDAO.changePasswordByLogin(user.getLogin(), hashedPassword);
                user.setPassword(hashedPassword);
                System.out.println("Password changed successfully.");
                return true;
            }
        }
        this.user = null;
        return false;
    }

    /**
     * Creates a rescuer with the provided details.
     * The rescuer is created only if there is no existing rescuer with the same ID.
     *
     * @param id The ID of the rescuer.
     * @param nom The name of the rescuer.
     * @param prenom The first name of the rescuer.
     * @param dateNaissance The birth date of the rescuer.
     * @param tel The phone number of the rescuer.
     * @param adresse The address of the rescuer.
     * @param photo The photo of the rescuer as a byte array.
     * @return boolean indicating success or failure of the creation.
     */
    public boolean createRescuer(long id, String nom, String prenom, String dateNaissance, String tel, String adresse, byte[] photo) {

        SecouristeDAO dao = DAOFactory.getSecouristeDAO();
        // Vérifie si un secouriste avec le même id existe déjà
        if (dao.findById(id) != null) {
            System.out.println("Un secouriste avec l'ID " + id + " existe déjà.");
            return false;
        }
        this.secouriste = new Secouriste(id, nom, prenom, dateNaissance, tel, adresse, photo);
        boolean ok = DAOFactory.getSecouristeDAO().insert(this.secouriste);
        if (ok) {
            System.out.println("Rescuer created successfully.");
        } else {
            System.out.println("Failed to create rescuer.");
        }
        return ok;
    }

    /**
     * Checks if the current rescuer is created.
     * A rescuer is considered created if their name is not null and not empty.
     *
     * @return boolean indicating whether the rescuer is created or not.
     */
    public boolean SecouristeIsCreated() {
        User currentUser = getInstanceAuthentificationManagement().getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        Secouriste secouriste = getSecouristeDAO().findById(currentUser.getIdUser());
        if (secouriste == null) {
            return false;
        }
        return secouriste.getNom() != null && !secouriste.getNom().isEmpty();
    }

    /**
     * Get the current user loaded
     *
     * @return the user
     */
    public User getCurrentUser() {
        return this.user;
    }


    /**
     * Disconnects the current user and clears login information
     */
    public void logOut() {
        // Clear user and secouriste objects
        this.user = null;
        this.secouriste = null;

        // Clear preferences to avoid reconnection issues
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(controller.auth.LoginController.class);
            // Instead of removing completely, we can set them to empty strings
            // This avoids potential permission issues on some systems
            prefs.put("lastEmail", "");
            prefs.put("lastPW", "");
            prefs.flush(); // Make sure changes are saved immediately
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression des préférences : " + e.getMessage());
        }
    }

    /**
     * Returns the name of the current user.
     * @return the name of the current user, or a message if not connected.
     */
    public String getCurrentUserName() {
        if (user == null) {
            return "Vous n'êtes pas connecté";
        }
        if ("rescuer".equals(user.getRole())) {
            Secouriste sec = getSecouristeDAO().findById(user.getIdUser());
            if (sec != null && sec.getNom() != null) {
                return sec.getNom();
            }
        }
        else if ("administrator".equals(user.getRole())) {
            Administrateur admin = getAdministrateurDAO().findById(user.getIdUser());
            if (admin != null && admin.getNom() != null) {
                return admin.getNom();
            }
        }
        return "";
    }

    /**
     * Changes the role of the current user.
     * This method updates the user's role in the database and sets it in the current user object.
     *
     * @param currentUser The user whose role is to be changed.
     * @param role The new role to be assigned to the user.
     */
    public void changeRole(User currentUser, String role) {
        currentUser.setRole(role);
        this.userDAO.changeRoleById(currentUser.getIdUser(), role);
    }
}
