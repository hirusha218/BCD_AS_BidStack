package lk.java.bcd.auction.session;

import jakarta.ejb.Stateful;
import jakarta.ejb.Remove;
import lk.java.bcd.auction.entity.User;
import java.io.Serializable;

@Stateful
public class UserSessionBean implements Serializable {

    private User currentUser;

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    @Remove
    public void remove() {
        // This method will be called by the container to destroy the bean instance
        // For example, after a timeout or explicit call from client after logout
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
