package ua.knu.csc.it.rdms;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class InMemoryDatabaseManager {
    private final Collection<Database> databases;

    public InMemoryDatabaseManager() {
        this.databases = new ArrayList<>();
    }

    public void createDatabase(String name) {
        if (hasDatabase(name)) {
            throw new IllegalArgumentException("Database with such name exists");
        } else {
            databases.add(new Database(name));
        }
    }

    public void createTable(@Nonnull String databaseName, String tableName) {
        getDatabase(databaseName)
            .ifPresentOrElse(database -> database.createTable(tableName),
                () -> {
                    throw new IllegalArgumentException("Database does not exist");
                });
    }

    public boolean hasDatabase(String name) {
        return getDatabase(name).isPresent();
    }

    private Optional<Database> getDatabase(String name) {
        return databases.stream()
            .filter(database -> database.getName().equals(name))
            .findAny();
    }
}
