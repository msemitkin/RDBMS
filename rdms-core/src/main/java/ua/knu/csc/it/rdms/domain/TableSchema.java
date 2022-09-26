package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.regex.Pattern;

public record TableSchema(Set<Column> columns) {

    private static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*$^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*$";

    public void validateRow(@Nonnull Row row) {
        long specifiedColumnsCount = columns.stream()
            .filter(column -> row.values().containsKey(column))
            .count();
        if (specifiedColumnsCount != columns.size()) {
            throw new IllegalArgumentException("Number of columns must match the schema");
        }
        row.values().forEach((this::validateValueType));
    }

    public void validateValueType(Column column, Object value) {
        boolean hasValidType = switch (column.type()) {
            case INTEGER -> value instanceof Integer;
            case CHAR -> value instanceof Character;
            case DOUBLE -> value instanceof Double;
            case STRING -> value instanceof String;
            case ENUM -> false;
            case EMAIL -> value instanceof String email && Pattern.matches(EMAIL_REGEXP, email);
        };
        if (!hasValidType) {
            throw new IllegalArgumentException("%s column has invalid value".formatted(column.name()));
        }
    }
}
