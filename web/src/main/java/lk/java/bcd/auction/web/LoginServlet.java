package lk.java.bcd.auction.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.java.bcd.auction.entity.User;
import lk.java.bcd.auction.session.UserSessionBean;
import lk.java.bcd.auction.session.UserService; // Corrected: This should be UserServiceBean or UserService for authenticateUser

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @EJB
    private UserService userService; // For calling authenticateUser

    @EJB
    private UserSessionBean userSessionBean; // Stateful session bean

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If signupSuccess parameter is present, set an attribute for the JSP
        if ("true".equals(request.getParameter("signupSuccess"))) {
            request.setAttribute("message", "Sign up successful! Please log in.");
        }
        // Forward to the login page (e.g., login.jsp)
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        try {
            // Call authenticateUser from UserServiceBean (which is an implementation of UserService)
            User authenticatedUser = userService.authenticateUser(username.trim(), password);

            if (authenticatedUser != null) {
                userSessionBean.login(authenticatedUser);
                LOGGER.log(Level.INFO, "User logged in successfully: {0}", username);
                // Redirect to a protected area, e.g., dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                LOGGER.log(Level.WARNING, "Login failed for user: {0}", username);
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during login for user: " + username, e);
            request.setAttribute("error", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}
