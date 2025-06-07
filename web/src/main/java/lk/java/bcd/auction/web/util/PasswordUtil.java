package lk.java.bcd.auction.web.util;

/**
 * Conceptual Password Utility.
 * In a real application, use a strong password hashing library like BCrypt or Argon2.
 * This implementation is a placeholder for demonstration purposes ONLY and is NOT secure.
 */
public class PasswordUtil {

    /**
     * Hashes a plain text password.
     * THIS IS A DUMMY IMPLEMENTATION AND IS NOT SECURE.
     * Replace with a strong hashing algorithm in a real application.
     *
     * @param plainPassword The password to hash.
     * @return A "hashed" representation of the password.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return null;
        }
        // IMPORTANT: This is NOT a secure hash. For demonstration only.
        // Real implementation: return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        System.err.println("WARNING: Using DUMMY password hashing. NOT FOR PRODUCTION.");
        return "hashed:" + plainPassword;
    }

    /**
     * Checks a plain text password against a stored "hashed" password.
     * THIS IS A DUMMY IMPLEMENTATION AND IS NOT SECURE.
     * Replace with the corresponding check function from your chosen hashing library.
     *
     * @param plainPassword The plain text password to check.
     * @param hashedPassword The stored "hashed" password.
     * @return true if the passwords "match" (dummy check), false otherwise.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        // IMPORTANT: This is NOT a secure check. For demonstration only.
        // Real implementation: return BCrypt.checkpw(plainPassword, hashedPassword);
        System.err.println("WARNING: Using DUMMY password checking. NOT FOR PRODUCTION.");
        return hashedPassword.equals("hashed:" + plainPassword);
    }
}
