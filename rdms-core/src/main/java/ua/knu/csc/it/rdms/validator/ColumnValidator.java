package ua.knu.csc.it.rdms.validator;

import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;

public class ColumnValidator {

    void validate(Object value, ColumnType columnType) {
        if (!columnType.isValid(value)) {
            throw new IllegalArgumentException(
                "%s value is not valid for type %s".formatted(value, columnType.getName()));
        }
    }

}
