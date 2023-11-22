package be.equals.authservice.exception;

public class NotAuthenticatedException extends SecurityException {

    public NotAuthenticatedException() {
        super("Not authenticated");
    }
}
