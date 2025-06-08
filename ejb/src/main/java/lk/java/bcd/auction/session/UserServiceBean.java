package lk.java.bcd.auction.session;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lk.java.bcd.auction.entity.User;
// Import the new PasswordUtil
import lk.java.bcd.auction.util.PasswordUtil;
import lk.java.bcd.auction.session.UserRegistrationException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@DeclareRoles({"USER", "ADMIN"})
public class UserServiceBean implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceBean.class.getName());

    @PersistenceContext(unitName = "auctionPU")
    private EntityManager em;

    @Override
    public User registerUser(User user) throws UserRegistrationException {
        if (findUserByUsername(user.getUsername()) != null) {
            LOGGER.log(Level.WARNING, "Attempt to register with existing username: {0}", user.getUsername());
            throw new UserRegistrationException("Username '" + user.getUsername() + "' already exists.");
        }

        TypedQuery<User> queryByEmail = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
        queryByEmail.setParameter("email", user.getEmail());
        try {
            User existingUserByEmail = queryByEmail.getSingleResult();
            if (existingUserByEmail != null) {
                LOGGER.log(Level.WARNING, "Attempt to register with existing email: {0}", user.getEmail());
                throw new UserRegistrationException("Email '" + user.getEmail() + "' already exists.");
            }
        } catch (NoResultException e) {
            // This is good, means email is not taken
        }

        // Hash the password before persisting using the new PasswordUtil
        // Assuming the password in the user object is plain text at this point
        // or that hashing is consistently applied before calling registerUser.
        // For clarity, if registerUser expects a pre-hashed password, that logic should be in the caller.
        // If it expects a raw password, it should hash it here.
        // The original User.java suggested passwordHash is already set.
        // Let's assume the caller of registerUser (e.g., SignUpServlet) will hash it.

        em.persist(user);
        LOGGER.log(Level.INFO, "Registered new user: {0}", user.getUsername());
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean isValidPassword(User user, String rawPassword) {
        if (user == null || user.getPasswordHash() == null || rawPassword == null) {
            return false;
        }
        // Use the new PasswordUtil for checking
        return PasswordUtil.checkPassword(rawPassword, user.getPasswordHash());
    }

    /**
     * Authenticates a user by username and raw password.
     *
     * @param username The username.
     * @param rawPassword The raw password.
     * @return The User object if authentication is successful, null otherwise.
     */
    public User authenticateUser(String username, String rawPassword) {
        User user = findUserByUsername(username);
        if (user != null && isValidPassword(user, rawPassword)) {
            return user;
        }
        return null;
    }
}
