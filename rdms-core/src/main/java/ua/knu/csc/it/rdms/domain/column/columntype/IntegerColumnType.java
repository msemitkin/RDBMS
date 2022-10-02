package ua.knu.csc.it.rdms.domain.column.columntype;

import java.util.Comparator;

public class IntegerColumnType extends ColumnType {

    public IntegerColumnType() {
        super("INTEGER");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof Integer;
    }

    @Override
    public Comparator<Object> getComparator() {
        return (o1, o2) -> {
            Integer o11 = (Integer) o1;
            Integer o21 = (Integer) o2;
            return o11.compareTo(o21);
        };
    }

    @Override
    public String toString() {
        return "IntegerColumnType{" +
               "typeName='" + typeName + '\'' +
               '}';
    }
}
