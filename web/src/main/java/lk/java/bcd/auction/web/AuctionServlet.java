package lk.java.bcd.auction.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.java.bcd.auction.entity.AuctionItem;
import lk.java.bcd.auction.entity.User;
import lk.java.bcd.auction.session.AuctionException;
import lk.java.bcd.auction.session.AuctionService;
import lk.java.bcd.auction.session.UserNotFoundException;
import lk.java.bcd.auction.session.UserRegistrationException;
import lk.java.bcd.auction.session.UserService;
import lk.java.bcd.auction.web.util.PasswordUtil;
import lk.java.bcd.auction.web.util.SessionManager;

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

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Default action
        }

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
                case "showCreateForm": // Added for explicit navigation to create form
                    // Potentially check if user is logged in before showing
                    if (loggedInUsername == null) {
                        request.setAttribute("errorMessage", "You must be logged in to create an auction.");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                        return;
                    }
                    // Forward to a JSP that contains the create auction form,
                    // or embed form in index.jsp and ensure AuctionServlet handles its display.
                    // For now, let's assume index.jsp has the form and list is default.
                    // This case can be removed if create form is part of index.jsp directly.
                    response.sendRedirect(request.getContextPath() + "/auction#createForm"); // Redirect to a part of index page
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
            request.getRequestDispatcher("/index.jsp").forward(request, response); // Or a dedicated error page
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
                request.getRequestDispatcher("/index.jsp").forward(request, response); // Or a specific error/notfound page
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
        request.setAttribute("loggedInUsername", loggedInUsername); // For header in response pages

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
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            // Forward to a relevant page, possibly the one where the action originated or a general error page
            // For example, if action was 'register', forward back to register.jsp
            String forwardPage = "/index.jsp"; // Default fallback
            if ("register".equals(action)) forwardPage = "/register.jsp";
            else if ("login".equals(action)) forwardPage = "/login.jsp";
            else if ("placeBid".equals(action)) forwardPage = "/WEB-INF/viewItem.jsp"; // Requires item to be re-loaded

            if("placeBid".equals(action) && request.getParameter("itemId") != null) {
                 // Special handling for placeBid to show the item page again with error
                AuctionItem item = auctionService.findAuctionItemById(Long.parseLong(request.getParameter("itemId")));
                request.setAttribute("auctionItem", item);
            }
            request.getRequestDispatcher(forwardPage).forward(request, response);
        }
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
            newUser.setRegistrationDate(new Date()); // UserService could also set this default

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

        // Using conceptual PasswordUtil.checkPassword - replace with secure method
        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            SessionManager.loginUser(request.getSession(), user); // Creates session if not exists
            response.sendRedirect(request.getContextPath() + "/auction");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void handleCreateAuction(HttpServletRequest request, HttpServletResponse response, String loggedInUsername)
            throws ServletException, IOException {
        if (loggedInUsername == null) {
            request.setAttribute("errorMessage", "You must be logged in to create an auction.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double startingPrice = Double.parseDouble(request.getParameter("startingPrice"));
            String endTimeStr = request.getParameter("endTime");

            if (name == null || name.trim().isEmpty() || description == null || description.trim().isEmpty() || endTimeStr == null || endTimeStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Missing required fields for creating auction.");
                 // Forward back to a page that might show the form again, or a generic error page
                // If create form is on index.jsp:
                List<AuctionItem> items = auctionService.getAllAuctionItems();
                request.setAttribute("auctionItems", items);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endTime = sdf.parse(endTimeStr);

            // Potentially set the User who created the auction if AuctionItem has an 'owner' field
            AuctionItem newItem = new AuctionItem(name, description, startingPrice, endTime);
            auctionService.createAuctionItem(newItem);

            request.setAttribute("successMessage", "Auction item '" + name + "' created successfully!");
            response.sendRedirect(request.getContextPath() + "/auction?successMessage=Auction+created");


        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Invalid date format for end time. Use yyyy-MM-dd HH:mm:ss");
            List<AuctionItem> items = auctionService.getAllAuctionItems();
            request.setAttribute("auctionItems", items);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid starting price format.");
            List<AuctionItem> items = auctionService.getAllAuctionItems();
            request.setAttribute("auctionItems", items);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }


    private void handlePlaceBid(HttpServletRequest request, HttpServletResponse response, String loggedInUsername)
            throws ServletException, IOException {
        String itemIdStr = request.getParameter("itemId");
        String bidAmountStr = request.getParameter("bidAmount");

        if (loggedInUsername == null) {
            request.setAttribute("errorMessage", "You must be logged in to place a bid.");
             // Try to redirect back to item view if possible, or login page
            String redirectUrl = request.getContextPath() + "/auction?action=login";
            if (itemIdStr != null) {
                redirectUrl += "&redirect=" + request.getContextPath() + "/auction?action=viewItem%26itemId=" + itemIdStr;
            }
            response.sendRedirect(redirectUrl);
            return;
        }

        if (itemIdStr == null || bidAmountStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID and Bid Amount are required.");
            return;
        }

        AuctionItem item = null;
        try {
            Long itemId = Long.parseLong(itemIdStr);
            double bidAmount = Double.parseDouble(bidAmountStr);

            item = auctionService.findAuctionItemById(itemId); // For setting it back in request if error
            if (item == null) { // Should ideally not happen if linking from valid page
                 request.setAttribute("errorMessage", "Auction item not found.");
                 request.getRequestDispatcher("/index.jsp").forward(request, response);
                 return;
            }

            auctionService.placeBid(itemId, bidAmount, loggedInUsername);
            request.setAttribute("successMessage", "Bid placed successfully!");
            response.sendRedirect(request.getContextPath() + "/auction?action=viewItem&itemId=" + itemId);

        } catch (NumberFormatException e) {
            request.setAttribute("auctionItem", item); // item might be null if parsing itemId failed first
            request.setAttribute("errorMessage", "Invalid Item ID or Bid Amount format.");
            request.getRequestDispatcher("/WEB-INF/viewItem.jsp").forward(request, response);
        } catch (AuctionException | UserNotFoundException e) {
            request.setAttribute("auctionItem", item);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/viewItem.jsp").forward(request, response);
        } catch (Exception e) { // Catch other generic exceptions from EJB
            request.setAttribute("auctionItem", item);
            request.setAttribute("errorMessage", "An unexpected error occurred while placing bid: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/viewItem.jsp").forward(request, response);
        }
    }
}
