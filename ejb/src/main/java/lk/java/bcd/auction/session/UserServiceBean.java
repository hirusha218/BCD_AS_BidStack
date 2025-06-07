package lk.java.bcd.auction.session;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lk.java.bcd.auction.entity.User;
// Explicit import for UserRegistrationException, even if in same package, for clarity
import lk.java.bcd.auction.session.UserRegistrationException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@DeclareRoles({"USER", "ADMIN"}) // Declaring roles that might interact with or be managed by this service
public class UserServiceBean implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceBean.class.getName());

    @PersistenceContext(unitName = "auctionPU")
    private EntityManager em;

    @Override
    public User registerUser(User user) throws UserRegistrationException {
        // Check for existing username
        if (findUserByUsername(user.getUsername()) != null) {
            LOGGER.log(Level.WARNING, "Attempt to register with existing username: {0}", user.getUsername());
            throw new UserRegistrationException("Username '" + user.getUsername() + "' already exists.");
        }

        // Check for existing email
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

        // Persist the new user
        // Assumes user.passwordHash is already securely hashed
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
            return null; // User not found
        }
    }

    @Override
    public boolean isValidPassword(User user, String rawPassword) {
        // This is a conceptual placeholder.
        // In a real application, you would use a secure password hashing library
        // to compare the rawPassword against user.getPasswordHash().
        // For example, using BCrypt:
        // return BCrypt.checkpw(rawPassword, user.getPasswordHash());

        // For this basic EJB, we'll simulate a simple check if the hash isn't null.
        // DO NOT USE THIS IN PRODUCTION.
        LOGGER.log(Level.WARNING, "isValidPassword in UserServiceBean is a placeholder and NOT secure for production.");
        if (user == null || user.getPasswordHash() == null || rawPassword == null) {
            return false;
        }
        // This is NOT a secure check. It's just to make the method runnable.
        // A real implementation would involve BCrypt.checkpw(rawPassword, user.getPasswordHash())
        // For example, if passwordHash was just the raw password (VERY BAD IDEA, but for placeholder):
        // return user.getPasswordHash().equals(rawPassword);

        // Since we assume passwordHash is already hashed, we can't directly compare here
        // without the actual hashing library (e.g. BCrypt) and its comparison method.
        // This method, as is, is not truly functional for password validation without that library.
        // It highlights that the EJB *could* do it if it had access to the hashing lib's check function.
        throw new UnsupportedOperationException("Password validation requires a hashing library and should be implemented securely.");
    }
}
