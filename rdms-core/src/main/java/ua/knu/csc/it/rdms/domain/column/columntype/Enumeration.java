package ua.knu.csc.it.rdms.domain.column.columntype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

public class Enumeration extends ColumnType {
    private final Set<String> allowedValues;

    @JsonCreator
    public Enumeration(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("allowedValues") Set<String> allowedValues
    ) {
        super(typeName);
        this.allowedValues = allowedValues;
    }


    //required for serialization
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
               ", typeName='" + typeName + '\'' +
               '}';
    }
}
