package ua.knu.csc.it.rdms.domain;

public class TableDoesNotExistException extends RuntimeException {

    public TableDoesNotExistException(String tableName) {
        super("Table '%s' does not exist".formatted(tableName));
    }
}
