package model.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordHashing - This class provides methods for hashing passwords and verifying them
 * using the BCrypt hashing algorithm.
 * It allows you to securely store passwords and check them against user input.
 *
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class PasswordHashing {

    /**
     * The cost factor for the BCrypt hashing algorithm.
     * Higher values increase the time it takes to hash a password, making it more secure.
     */
    private static final int COST = 12; // you can tweak this

    /**
     * Hashes a plain password using the BCrypt algorithm.
     *
     * @param plainPassword The plain text password to be hashed.
     * @return The hashed password as a String.
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST));
    }

    /**
     * Verifies a plain password against a hashed password.
     *
     * @param plainPassword The plain text password to verify.
     * @param hashedPassword The hashed password to check against.
     * @return true if the plain password matches the hashed password, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
