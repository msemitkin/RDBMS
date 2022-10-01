package ua.knu.csc.it.rdms.domain.column.columntype;

public class StringColumnType extends ColumnType {
    public StringColumnType() {
        super("STRING");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof String;
    }

    @Override
    public String toString() {
        return "StringColumnType{" +
               "name='" + name + '\'' +
               '}';
    }
}
