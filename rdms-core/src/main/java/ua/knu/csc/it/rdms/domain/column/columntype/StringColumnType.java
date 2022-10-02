package ua.knu.csc.it.rdms.domain.column.columntype;

import java.util.Comparator;

public class StringColumnType extends ColumnType {
    public StringColumnType() {
        super("STRING");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof String;
    }

    @Override
    public Comparator<Object> getComparator() {
        return (o1, o2) -> {
            String o11 = (String) o1;
            String o21 = (String) o2;
            return o11.compareTo(o21);
        };
    }

    @Override
    public String toString() {
        return "StringColumnType{" +
               "name='" + typeName + '\'' +
               '}';
    }
}
