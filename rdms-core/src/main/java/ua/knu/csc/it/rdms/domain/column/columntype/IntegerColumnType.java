package ua.knu.csc.it.rdms.domain.column.columntype;

public class IntegerColumnType extends ColumnType {

    public IntegerColumnType() {
        super("INTEGER");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof Integer;
    }
}
