package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

public record Table(@Nonnull String name, Set<Column> columns) {

    public Table {
        if (columns.isEmpty()) {
            throw new IllegalArgumentException("Table must have at least one column");
        }

        int numberOfUniqueNames = columns.stream()
            .map(Column::name)
            .collect(Collectors.toSet())
            .size();
        if (numberOfUniqueNames != columns.size()) {
            throw new IllegalArgumentException("Column names must be unique");
        }
    }

}
