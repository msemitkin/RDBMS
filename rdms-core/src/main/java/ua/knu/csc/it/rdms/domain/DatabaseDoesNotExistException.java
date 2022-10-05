package ua.knu.csc.it.rdms.domain;

public class DatabaseDoesNotExistException extends RuntimeException {
    public DatabaseDoesNotExistException(String message) {
        super(message);
    }
}
