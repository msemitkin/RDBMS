package ua.knu.csc.it.rdms.domain.validator;

public class ColumnTypeMismatchException extends RuntimeException {
    private final String column;
    private final String expectedType;

    public ColumnTypeMismatchException(
        String column,
        String expectedType
    ) {
        this.column = column;
        this.expectedType = expectedType;
    }

    public String getColumn() {
        return column;
    }

    public String getExpectedType() {
        return expectedType;
    }

}
