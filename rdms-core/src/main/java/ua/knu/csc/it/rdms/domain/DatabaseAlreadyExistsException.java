package ua.knu.csc.it.rdms.domain;

public class DatabaseAlreadyExistsException extends RuntimeException {
    private final String databaseName;

    public DatabaseAlreadyExistsException(String databaseName) {
        super("Database %s already exists".formatted(databaseName));
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
