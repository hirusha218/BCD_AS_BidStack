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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DashboardServlet.class.getName());

    @EJB
    private UserSessionBean userSessionBean; // Stateful session bean for logged-in user info

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (userSessionBean.isLoggedIn()) {
            User currentUser = userSessionBean.getCurrentUser();
            request.setAttribute("username", currentUser.getUsername());
            request.setAttribute("email", currentUser.getEmail());
            request.setAttribute("registrationDate", currentUser.getRegistrationDate());

            LOGGER.info("Dashboard accessed by user: " + currentUser.getUsername());
            request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
        } else {
            LOGGER.warning("Dashboard access attempt by non-loggedin user. Redirecting to login.");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
