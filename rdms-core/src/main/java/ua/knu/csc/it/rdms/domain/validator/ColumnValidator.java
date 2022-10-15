package ua.knu.csc.it.rdms.domain.validator;

import org.springframework.stereotype.Component;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;

@Component
public class ColumnValidator {

    public void validate(Object value, Column column) {
        ColumnType columnType = column.type();
        if (!columnType.isValid(value)) {
            throw new ColumnTypeMismatchException(
                column.name(),
                columnType.getTypeName()
            );
        }
    }

}
