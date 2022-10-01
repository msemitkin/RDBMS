package ua.knu.csc.it.rdms.domain.column.columntype;

public class IntegerColumnType extends ColumnType {

    public IntegerColumnType() {
        super("INTEGER");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof Integer;
    }

    @Override
    public String toString() {
        return "IntegerColumnType{" +
               "typeName='" + typeName + '\'' +
               '}';
    }
}
