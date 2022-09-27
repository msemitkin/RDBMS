package ua.knu.csc.it.rdms.validator;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.TableSchema;

import javax.annotation.Nonnull;

public class RowValidator {
    private final ColumnValidator columnValidator;

    public RowValidator(ColumnValidator columnValidator) {
        this.columnValidator = columnValidator;
    }

    public void validateRow(@Nonnull Row row, @Nonnull TableSchema tableSchema) {
        long specifiedColumnsCount = tableSchema.columns().stream()
            .filter(column -> row.values().containsKey(column))
            .count();
        if (specifiedColumnsCount != tableSchema.columns().size()) {
            throw new IllegalArgumentException("Number of columns must match the schema");
        }
        row.values().forEach((column, value) -> columnValidator.validate(value, column.type()));
    }

}
