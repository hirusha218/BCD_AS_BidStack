package lk.java.bcd.auction.session;

public class UserRegistrationException extends Exception {
    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
