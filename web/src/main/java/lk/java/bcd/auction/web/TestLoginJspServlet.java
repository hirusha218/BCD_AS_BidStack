package lk.java.bcd.auction.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/testLoginJsp")
public class TestLoginJspServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TestLoginJspServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.info("TestLoginJspServlet doGet called. Attempting to forward to /WEB-INF/jsp/login.jsp");

        // Set a test attribute to see if it reaches the JSP
        request.setAttribute("testMessage", "Message from TestLoginJspServlet");

        try {
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            LOGGER.info("Forward to /WEB-INF/jsp/login.jsp successful from TestLoginJspServlet.");
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error forwarding to /WEB-INF/jsp/login.jsp from TestLoginJspServlet", e);
            // Output an error message to the response if forward fails catastrophically
            response.setContentType("text/plain");
            response.getWriter().println("Error in TestLoginJspServlet: Could not forward to login.jsp. Check server logs.");
            response.getWriter().println("Exception: " + e.getMessage());
        }
    }
}
