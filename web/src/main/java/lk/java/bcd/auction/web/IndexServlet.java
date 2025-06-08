package lk.java.bcd.auction.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.java.bcd.auction.entity.User;
import lk.java.bcd.auction.session.UserSessionBean;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/index") // Will also be mapped as a welcome file if configured in web.xml
public class IndexServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(IndexServlet.class.getName());

    @EJB
    private UserSessionBean userSessionBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.info("IndexServlet doGet called.");

        if (userSessionBean != null && userSessionBean.isLoggedIn()) {
            User currentUser = userSessionBean.getCurrentUser();
            if (currentUser != null) {
                request.setAttribute("username", currentUser.getUsername());
                LOGGER.info("User " + currentUser.getUsername() + " is logged in.");
            } else {
                // This case should ideally not happen if isLoggedIn is true, but as a safeguard:
                request.setAttribute("username", null);
                LOGGER.info("User is marked as logged in, but no user object found in session bean.");
            }
            request.setAttribute("isLoggedIn", true);
        } else {
            request.setAttribute("isLoggedIn", false);
            LOGGER.info("User is not logged in.");
        }

        try {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error forwarding to /WEB-INF/jsp/index.jsp from IndexServlet", e);
            // Handle error, maybe show a generic error page or send HTTP error
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not display the index page.");
        }
    }
}
