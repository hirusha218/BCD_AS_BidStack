package lk.java.bcd.auction.web.util;

import jakarta.servlet.http.HttpSession;
import lk.java.bcd.auction.entity.User; // Assuming User entity might be used or a UserDTO

/**
 * SessionManager provides utility methods for managing user sessions in the web tier.
 * This class is conceptual and its methods are intended to be called from Servlets,
 * JAX-RS resources, or Filters that have access to the HttpSession object.
 *
 * In a real application, consider the security implications of what is stored in the session.
 * Storing minimal, non-sensitive identifiers is often preferred.
 */
public class SessionManager {

    private static final String LOGGED_IN_USER_ATTRIBUTE = "loggedInUsername";
    // Alternatively, a more complex UserDTO object could be stored.
    // private static final String LOGGED_IN_USER_DTO_ATTRIBUTE = "loggedInUserDTO";


    /**
     * Marks a user as logged in by storing their identifier in the HTTP session.
     *
     * @param session The HttpSession object from the current request.
     * @param user The authenticated User object. It's recommended to store only necessary,
     *             non-sensitive information like username or a dedicated UserDTO.
     *             For this example, we store the username.
     */
    public static void loginUser(HttpSession session, User user) {
        if (session != null && user != null) {
            // Invalidate any existing session to prevent session fixation attacks,
            // then create a new session. However, typical usage is to use the provided session
            // after authentication and just set attributes. Re-evaluate if session fixation
            // is a high concern for the specific authentication flow.
            // For simplicity here, we just set the attribute on the existing session.
            session.setAttribute(LOGGED_IN_USER_ATTRIBUTE, user.getUsername());
            // Example if storing a UserDTO:
            // UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
            // session.setAttribute(LOGGED_IN_USER_DTO_ATTRIBUTE, userDTO);
        }
    }

    /**
     * Logs out the current user by invalidating their HTTP session.
     *
     * @param session The HttpSession object from the current request.
     */
    public static void logoutUser(HttpSession session) {
        if (session != null) {
            session.removeAttribute(LOGGED_IN_USER_ATTRIBUTE);
            // session.removeAttribute(LOGGED_IN_USER_DTO_ATTRIBUTE); // if using DTO
            session.invalidate();
        }
    }

    /**
     * Retrieves the username of the currently logged-in user from the session.
     *
     * @param session The HttpSession object from the current request.
     * @return The username of the logged-in user, or null if no user is logged in
     *         or the session is invalid.
     */
    public static String getLoggedInUsername(HttpSession session) {
        if (session != null) {
            Object userAttribute = session.getAttribute(LOGGED_IN_USER_ATTRIBUTE);
            if (userAttribute instanceof String) {
                return (String) userAttribute;
            }
            // Example if retrieving a UserDTO:
            /*
            Object userDTOAttribute = session.getAttribute(LOGGED_IN_USER_DTO_ATTRIBUTE);
            if (userDTOAttribute instanceof UserDTO) {
                return ((UserDTO) userDTOAttribute).getUsername();
            }
            */
        }
        return null;
    }

    /**
     * Checks if a user is currently marked as logged in via the session.
     *
     * @param session The HttpSession object from the current request.
     * @return true if a user is logged in, false otherwise.
     */
    public static boolean isUserLoggedIn(HttpSession session) {
        return getLoggedInUsername(session) != null;
    }

    // Conceptual UserDTO - if you prefer to store more structured (but still minimal) user info
    /*
    public static class UserDTO implements java.io.Serializable {
        private Long id;
        private String username;
        private String email;
        // Add other non-sensitive fields as needed

        public UserDTO(Long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        // Ensure it's serializable if session persistence/replication is used.
    }
    */
}
