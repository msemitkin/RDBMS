package ua.knu.csc.it.rdms.domain;

public class TableAlreadyExistsException extends RuntimeException {
    private final String name;

    public TableAlreadyExistsException(String name) {
        super("Table %s already exists".formatted(name));
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
