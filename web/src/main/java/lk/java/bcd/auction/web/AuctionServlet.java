package lk.java.bcd.auction.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.java.bcd.auction.entity.AuctionItem;
import lk.java.bcd.auction.session.AuctionService;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/auction")
public class AuctionServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AuctionServlet.class.getName());

    @EJB
    private AuctionService auctionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Auction Items</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Current Auction Items</h1>");

            List<AuctionItem> items = auctionService.getAllAuctionItems();
            if (items == null || items.isEmpty()) {
                out.println("<p>No auction items found.</p>");
            } else {
                out.println("<ul>");
                for (AuctionItem item : items) {
                    out.println("<li>" + item.getName() + " (" + item.getDescription() + ") - Current Price: $"
                            + String.format("%.2f", item.getCurrentPrice()) + " - Ends: " + item.getEndTime() + "</li>");
                }
                out.println("</ul>");
            }

            out.println("<h2>Create New Auction Item</h2>");
            out.println("<form method='post'>");
            out.println("Name: <input type='text' name='name' required><br>");
            out.println("Description: <input type='text' name='description' required><br>");
            out.println("Starting Price: <input type='number' step='0.01' name='startingPrice' required><br>");
            out.println("End Time (yyyy-MM-dd HH:mm:ss): <input type='text' name='endTime' placeholder='e.g., 2024-12-31 23:59:59' required><br>");
            out.println("<input type='submit' value='Create Item'>");
            out.println("</form>");

            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doGet of AuctionServlet", e);
            throw new ServletException("Error retrieving auction items", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double startingPrice = Double.parseDouble(request.getParameter("startingPrice"));
            String endTimeStr = request.getParameter("endTime");

            if (name == null || name.trim().isEmpty() ||
                description == null || description.trim().isEmpty() ||
                endTimeStr == null || endTimeStr.trim().isEmpty()) {
                // Or handle error more gracefully
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required form fields.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endTime;
            try {
                endTime = sdf.parse(endTimeStr);
            } catch (ParseException e) {
                LOGGER.log(Level.WARNING, "Invalid date format for endTime: " + endTimeStr, e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format for End Time. Please use yyyy-MM-dd HH:mm:ss.");
                return;
            }


            AuctionItem newItem = new AuctionItem(name, description, startingPrice, endTime);
            auctionService.createAuctionItem(newItem);

            LOGGER.log(Level.INFO, "Created new auction item: {0}", name);
            response.sendRedirect(request.getContextPath() + "/auction");

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid number format for startingPrice", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid starting price format.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doPost of AuctionServlet", e);
            throw new ServletException("Error creating auction item", e);
        }
    }
}
