package ua.knu.csc.it.rdms.domain.column.columntype;

import java.util.Objects;

public abstract class ColumnType {
    protected final String typeName;

    protected ColumnType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public abstract boolean isValid(Object value);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnType that = (ColumnType) o;
        return Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName);
    }

    @Override
    public String toString() {
        return "ColumnType{" +
               "name='" + typeName + '\'' +
               '}';
    }
}
