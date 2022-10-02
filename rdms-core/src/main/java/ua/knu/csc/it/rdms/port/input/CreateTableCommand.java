package ua.knu.csc.it.rdms.port.input;


import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

public record CreateTableCommand(@Nonnull String name, Set<TableColumn> tableColumns) {

    public CreateTableCommand {
        if (tableColumns.isEmpty()) {
            throw new IllegalArgumentException("SaveTableCommand must have at least one column");
        }

        int numberOfUniqueNames = tableColumns.stream()
            .map(TableColumn::name)
            .collect(Collectors.toSet())
            .size();
        if (numberOfUniqueNames != tableColumns.size()) {
            throw new IllegalArgumentException("Column names must be unique");
        }
    }

}
