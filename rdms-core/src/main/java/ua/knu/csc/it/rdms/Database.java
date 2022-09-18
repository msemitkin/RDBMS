package ua.knu.csc.it.rdms;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class Database {
    private final String name;
    private final Collection<Table> tables;

    public Database(@Nonnull String name) {
        this.name = Objects.requireNonNull(name);
        this.tables = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void createTable(String name) {
        getTable(name).ifPresentOrElse(s -> {
            throw new IllegalArgumentException("Table with such name already exists");
        }, () -> tables.add(new Table(name)));
    }

    private Optional<Table> getTable(String tableName) {
        return tables.stream()
            .filter(table -> table.getName().equals(tableName))
            .findAny();
    }

}
