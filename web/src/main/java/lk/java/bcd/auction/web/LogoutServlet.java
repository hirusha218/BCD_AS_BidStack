package lk.java.bcd.auction.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.java.bcd.auction.session.UserSessionBean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());

    @EJB
    private UserSessionBean userSessionBean; // Stateful session bean

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String username = null;
            if (userSessionBean.isLoggedIn()) {
                username = userSessionBean.getCurrentUser().getUsername();
            }

            userSessionBean.logout(); // Clear user from SFSB
            // Optionally call the @Remove method if you want to explicitly destroy the SFSB instance
            // userSessionBean.remove();

            HttpSession session = request.getSession(false); // Get session if it exists, don't create new
            if (session != null) {
                session.invalidate(); // Invalidate the HTTP session
            }

            if (username != null) {
                LOGGER.log(Level.INFO, "User logged out successfully: {0}", username);
            } else {
                LOGGER.log(Level.INFO, "Logout called, no active user in UserSessionBean.");
            }

            response.sendRedirect(request.getContextPath() + "/login?logoutSuccess=true");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during logout", e);
            // Even if error, try to redirect to login
            response.sendRedirect(request.getContextPath() + "/login?logoutError=true");
        }
    }
}
