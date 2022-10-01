package ua.knu.csc.it.rdms.domain.validator;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.domain.column.Column;

import javax.annotation.Nonnull;
import java.util.Set;

public class RowValidator {
    private final ColumnValidator columnValidator;

    public RowValidator(ColumnValidator columnValidator) {
        this.columnValidator = columnValidator;
    }

    public void validateRow(@Nonnull Row row, @Nonnull TableSchema tableSchema) {
        Set<Column> schemaColumns = tableSchema.columns();
        Set<Column> rowColumns = row.values().keySet();
        boolean rowColumnsAreValid = schemaColumns.containsAll(rowColumns)
                                     && rowColumns.containsAll(schemaColumns);
        if (!rowColumnsAreValid) {
            throw new IllegalArgumentException("Row not match with schema");
        }
        row.values().forEach((column, value) -> columnValidator.validate(value, column.type()));
    }

}
