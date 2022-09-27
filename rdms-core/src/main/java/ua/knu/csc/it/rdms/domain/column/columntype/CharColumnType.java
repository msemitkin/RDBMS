package ua.knu.csc.it.rdms.domain.column.columntype;

public class CharColumnType extends ColumnType {
    public CharColumnType() {
        super("CHAR");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof Character;
    }
}
