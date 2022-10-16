package ua.knu.csc.it.rdms.domain;

public class UnknownColumnException extends RuntimeException {
    private final String columnName;

    public UnknownColumnException(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
