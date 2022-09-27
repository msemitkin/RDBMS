package ua.knu.csc.it.rdms.domain.column.columntype;

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
}
