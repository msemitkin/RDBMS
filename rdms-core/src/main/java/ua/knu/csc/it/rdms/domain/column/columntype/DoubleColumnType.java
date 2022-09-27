package ua.knu.csc.it.rdms.domain.column.columntype;

public class DoubleColumnType extends ColumnType {
    public DoubleColumnType() {
        super("DOUBLE");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof Double;
    }
}
