package ua.knu.csc.it.rdms.domain.column.columntype;

import java.util.Objects;
import java.util.Set;

public class Enumeration extends ColumnType {
    private final Set<String> allowedValues;

    public Enumeration(String name, Set<String> allowedValues) {
        super(name);
        this.allowedValues = allowedValues;
    }

    public Set<String> getAllowedValues() {
        return allowedValues;
    }


    @Override
    public boolean isValid(Object value) {
        return value instanceof String stringValue && allowedValues.contains(stringValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Enumeration that = (Enumeration) o;
        return Objects.equals(allowedValues, that.allowedValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), allowedValues);
    }

    @Override
    public String toString() {
        return "Enumeration{" +
               "allowedValues=" + allowedValues +
               ", name='" + name + '\'' +
               '}';
    }
}
