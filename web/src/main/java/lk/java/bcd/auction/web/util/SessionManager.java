package lk.java.bcd.auction.web.util;

import jakarta.servlet.http.HttpSession;
import lk.java.bcd.auction.entity.User;

public class SessionManager {

    private static final String LOGGED_IN_USER_ATTRIBUTE = "loggedInUsername";

    public static void loginUser(HttpSession session, User user) {
        if (session != null && user != null) {
            session.setAttribute(LOGGED_IN_USER_ATTRIBUTE, user.getUsername());
        }
    }

    public static void logoutUser(HttpSession session) {
        if (session != null) {
            session.removeAttribute(LOGGED_IN_USER_ATTRIBUTE);
            session.invalidate();
        }
    }

    public static String getLoggedInUsername(HttpSession session) {
        if (session != null) {
            Object userAttribute = session.getAttribute(LOGGED_IN_USER_ATTRIBUTE);
            if (userAttribute instanceof String) {
                return (String) userAttribute;
            }
        }
        return null;
    }

    public static boolean isUserLoggedIn(HttpSession session) {
        return getLoggedInUsername(session) != null;
    }
}
