package lk.java.bcd.auction.session;

import jakarta.ejb.Local;
import lk.java.bcd.auction.entity.User;

@Local
public interface UserService {

    /**
     * Registers a new user.
     * Assumes the password in the User object is already securely hashed.
     * @param user The user object with pre-hashed password.
     * @return The persisted User object with its generated ID.
     * @throws UserRegistrationException if username or email already exists.
     */
    User registerUser(User user) throws UserRegistrationException;

    /**
     * Finds a user by their username.
     * @param username The username to search for.
     * @return The User object if found, otherwise null.
     */
    User findUserByUsername(String username);

    /**
     * (Conceptual) Validates a given raw password against the user's stored hashed password.
     * Actual implementation of password comparison (e.g., BCrypt.checkpw) would happen here
     * if not handled by a declarative security mechanism.
     *
     * @param user The user object containing the hashed password.
     * @param rawPassword The raw password to validate.
     * @return true if the password is valid, false otherwise.
     */
    boolean isValidPassword(User user, String rawPassword);
    // This method is more for conceptual placement. In a real app,
    // password validation often happens closer to the point of credential submission (web/API tier)
    // or is handled by Jakarta Security.
}

// Custom exception for registration issues
class UserRegistrationException extends Exception {
    public UserRegistrationException(String message) {
        super(message);
    }
}
