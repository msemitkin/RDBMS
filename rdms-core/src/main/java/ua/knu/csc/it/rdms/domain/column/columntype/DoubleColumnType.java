package ua.knu.csc.it.rdms.domain.column.columntype;

import java.util.Comparator;

public class DoubleColumnType extends ColumnType {
    public DoubleColumnType() {
        super("DOUBLE");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof Double;
    }

    @Override
    public Comparator<Object> getComparator() {
        return (o1, o2) -> {
            Double o11 = (Double) o1;
            Double o21 = (Double) o2;
            return o11.compareTo(o21);
        };
    }

    @Override
    public String toString() {
        return "DoubleColumnType{" +
               "typeName='" + typeName + '\'' +
               '}';
    }
}
