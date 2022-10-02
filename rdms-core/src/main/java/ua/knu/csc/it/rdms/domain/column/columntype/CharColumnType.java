package ua.knu.csc.it.rdms.domain.column.columntype;

import java.util.Comparator;

public class CharColumnType extends ColumnType {
    public CharColumnType() {
        super("CHAR");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof Character;
    }

    @Override
    public Comparator<Object> getComparator() {
        return (o1, o2) -> {
            Character o11 = (Character) o1;
            Character o21 = (Character) o2;
            return o11.compareTo(o21);
        };
    }

    @Override
    public String toString() {
        return "CharColumnType{" +
               "typeName='" + typeName + '\'' +
               '}';
    }

}
