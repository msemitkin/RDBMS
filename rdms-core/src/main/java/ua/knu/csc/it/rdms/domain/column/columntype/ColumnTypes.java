package ua.knu.csc.it.rdms.domain.column.columntype;

import javax.annotation.Nonnull;
import java.util.Set;

public record ColumnTypes(Set<ColumnType> columnTypes) {
    public ColumnType getByName(@Nonnull String name) {
        return columnTypes.stream()
            .filter(type -> type.getTypeName().equals(name))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Invalid column type: %s".formatted(name)));
    }
}
