package ua.knu.csc.it.rdms.domain.column.columntype;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class ColumnType {
    protected final String name;

    protected ColumnType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isValid(Object value);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnType that = (ColumnType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ColumnType{" +
               "name='" + name + '\'' +
               '}';
    }
}
