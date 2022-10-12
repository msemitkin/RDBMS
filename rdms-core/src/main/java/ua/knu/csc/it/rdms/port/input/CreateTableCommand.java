package ua.knu.csc.it.rdms.port.input;


import ua.knu.csc.it.rdms.domain.ValidationException;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

public record CreateTableCommand(@Nonnull String name, Set<TableColumn> tableColumns) {

    public CreateTableCommand {
        name = name.trim();

        if (tableColumns.isEmpty()) {
            throw new ValidationException("Table must have at least one column");
        }

        tableColumns = tableColumns.stream()
            .map(it -> new TableColumn(it.type().toUpperCase(), it.name()))
            .collect(Collectors.toSet());

        int numberOfUniqueNames = tableColumns.stream()
            .map(TableColumn::name)
            .collect(Collectors.toSet())
            .size();
        if (numberOfUniqueNames != tableColumns.size()) {
            throw new IllegalArgumentException("Column names must be unique");
        }
    }

}
