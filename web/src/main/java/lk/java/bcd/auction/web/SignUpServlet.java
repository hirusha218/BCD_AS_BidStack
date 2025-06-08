package lk.java.bcd.auction.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.java.bcd.auction.entity.User;
import lk.java.bcd.auction.session.UserSessionBean;
import lk.java.bcd.auction.session.UserService;
import lk.java.bcd.auction.session.UserRegistrationException;
import lk.java.bcd.auction.util.PasswordUtil; // EJB module PasswordUtil

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SignUpServlet.class.getName());

    @EJB
    private UserService userService;

    @EJB
    private UserSessionBean userSessionBean;

    // Note: PasswordUtil is now in the EJB module (lk.java.bcd.auction.util.PasswordUtil)
    // Static methods will be called directly. No need to inject if all methods are static.

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the signup page (e.g., signup.jsp)
        request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            request.setAttribute("error", "Username, email, and password are required.");
            request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
            return;
        }

        try {
            User newUser = new User();
            newUser.setUsername(username.trim());
            newUser.setEmail(email.trim());
            // Hash the password using the PasswordUtil from the EJB module
            newUser.setPasswordHash(PasswordUtil.hashPassword(password));
            newUser.setRegistrationDate(new Date());

            userService.registerUser(newUser);
            userSessionBean.login(newUser); // Log in the user
            LOGGER.log(Level.INFO, "User registered successfully: {0}", username);
            // Redirect to index page
            response.sendRedirect(request.getContextPath() + "/index");

        } catch (UserRegistrationException e) {
            LOGGER.log(Level.WARNING, "User registration failed: " + e.getMessage());
            request.setAttribute("error", e.getMessage()); // Display specific error from EJB
            request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during user registration", e);
            request.setAttribute("error", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(request, response);
        }
    }
}
