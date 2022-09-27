package ua.knu.csc.it.rdms.domain.column.columntype;

public abstract class ColumnType {
    protected final String name;

    protected ColumnType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isValid(Object value);

}
