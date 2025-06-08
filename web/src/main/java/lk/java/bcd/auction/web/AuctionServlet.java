package lk.java.bcd.auction.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.java.bcd.auction.entity.AuctionItem;
import lk.java.bcd.auction.entity.User;
import lk.java.bcd.auction.session.*;
import lk.java.bcd.auction.web.util.PasswordUtil;
import lk.java.bcd.auction.web.util.SessionManager;

import java.io.IOException;
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

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        String loggedInUsername = SessionManager.getLoggedInUsername(request.getSession(false));
        request.setAttribute("loggedInUsername", loggedInUsername);

        try {
            switch (action) {
                case "register":
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                    break;
                case "login":
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    break;
                case "logout":
                    SessionManager.logoutUser(request.getSession(false));
                    response.sendRedirect(request.getContextPath() + "/auction");
                    break;
                case "viewItem":
                    handleViewItem(request, response);
                    break;
                case "showCreateForm":
                    if (loggedInUsername == null) {
                        request.setAttribute("errorMessage", "You must be logged in to create an auction.");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                        return;
                    }
                    request.getRequestDispatcher("/createAuction.jsp").forward(request, response);
                    break;
                case "list":
                default:
                    List<AuctionItem> items = auctionService.getAllAuctionItems();
                    request.setAttribute("auctionItems", items);
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in AuctionServlet doGet", e);
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private void handleViewItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemIdStr = request.getParameter("itemId");
        if (itemIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID is required.");
            return;
        }

        try {
            Long itemId = Long.parseLong(itemIdStr);
            AuctionItem item = auctionService.findAuctionItemById(itemId);
            if (item == null) {
                request.setAttribute("errorMessage", "Auction item not found.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }
            request.setAttribute("auctionItem", item);
            request.getRequestDispatcher("/WEB-INF/viewItem.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Item ID format.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Form action not specified.");
            return;
        }

        String loggedInUsername = SessionManager.getLoggedInUsername(request.getSession(false));
        request.setAttribute("loggedInUsername", loggedInUsername);

        try {
            switch (action) {
                case "register":
                    handleRegister(request, response);
                    break;
                case "login":
                    handleLogin(request, response);
                    break;
                case "placeBid":
                    handlePlaceBid(request, response, loggedInUsername);
                    break;
                case "createAuction":
                    handleCreateAuction(request, response, loggedInUsername);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown form action.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in AuctionServlet doPost for action: " + action, e);
            String forwardPage = "/index.jsp";
            if ("register".equals(action)) forwardPage = "/register.jsp";
            else if ("login".equals(action)) forwardPage = "/login.jsp";
            else if ("placeBid".equals(action)) forwardPage = "/WEB-INF/viewItem.jsp";

            if ("placeBid".equals(action) && request.getParameter("itemId") != null) {
                AuctionItem item = auctionService.findAuctionItemById(Long.parseLong(request.getParameter("itemId")));
                request.setAttribute("auctionItem", item);
            }

            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher(forwardPage).forward(request, response);
        }
    }

    private void handlePlaceBid(HttpServletRequest request, HttpServletResponse response, String loggedInUsername) {
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            String hashedPassword = PasswordUtil.hashPassword(password);
            User newUser = new User(username, hashedPassword, email);
            newUser.setRegistrationDate(new Date());

            userService.registerUser(newUser);
            request.setAttribute("message", "Registration successful! Please login.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (UserRegistrationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.findUserByUsername(username);
        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            SessionManager.loginUser(request.getSession(true), user);
            response.sendRedirect(request.getContextPath() + "/auction");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void handlePlaceBid(HttpServletRequest request, HttpServletResponse response, double username)
            throws ServletException, IOException {
        if (username == Double.parseDouble(null)) {
            request.setAttribute("errorMessage", "You must be logged in to place a bid.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            Long itemId = Long.parseLong(request.getParameter("itemId"));
            double bidAmount = Double.parseDouble(request.getParameter("bidAmount"));
            auctionService.placeBid(itemId, username, bidAmount);
            response.sendRedirect(request.getContextPath() + "/auction?action=viewItem&itemId=" + itemId);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Failed to place bid: " + e.getMessage());
            handleViewItem(request, response);
        }
    }

    private void handleCreateAuction(HttpServletRequest request, HttpServletResponse response, String username)
            throws ServletException, IOException {
        if (username == null) {
            request.setAttribute("errorMessage", "You must be logged in to create an auction.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            double startingPrice = Double.parseDouble(request.getParameter("startingPrice"));
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("endDate"));

            AuctionItem item = new AuctionItem();

            item.setDescription(description);
            item.setStartingPrice(startingPrice);

            auctionService.createAuctionItem(item);
            response.sendRedirect(request.getContextPath() + "/auction");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Failed to create auction: " + e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}
