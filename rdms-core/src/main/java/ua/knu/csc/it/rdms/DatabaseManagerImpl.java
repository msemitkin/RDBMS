package ua.knu.csc.it.rdms;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.port.output.DatabasePersistenceManager;

import javax.annotation.Nonnull;
import java.util.List;

public class DatabaseManagerImpl implements DatabaseManager {
    private final DatabasePersistenceManager databasePersistenceManager;

    public DatabaseManagerImpl(DatabasePersistenceManager databasePersistenceManager) {
        this.databasePersistenceManager = databasePersistenceManager;
    }

    @Override
    public void createDatabase(@Nonnull String name) {
        if (databasePersistenceManager.existsDatabase(name)) {
            throw new IllegalArgumentException("Database %s already exists".formatted(name));
        }
        databasePersistenceManager.saveDatabase(name);
    }

    @Override
    public void createTable(@Nonnull String database, Table table) {
        if (!databasePersistenceManager.existsDatabase(database)) {
            throw new IllegalArgumentException("Database %s does not exist".formatted(database));
        }
        if (databasePersistenceManager.existsTable(database, table.name())) {
            throw new IllegalArgumentException("Table %s already exists".formatted(table.name()));
        }
        databasePersistenceManager.saveTable(database, table);
    }

    @Override
    public void dropTable(String database, String table) {
        if (!databasePersistenceManager.existsDatabase(database)) {
            throw new IllegalArgumentException("Database %s does not exist".formatted(database));
        }
        if (!databasePersistenceManager.existsTable(database, table)) {
            throw new IllegalArgumentException("Table %s does not exist".formatted(table));
        }
        databasePersistenceManager.deleteTable(database, table);
    }

    @Override
    public void insert(String database, String table, Row row) {
        if (!databasePersistenceManager.existsDatabase(database)) {
            throw new IllegalArgumentException("Database %s does not exist".formatted(database));
        }
        if (!databasePersistenceManager.existsTable(database, table)) {
            throw new IllegalArgumentException("Table %s does not exist".formatted(table));
        }
        validateRow(database, table, row);
        databasePersistenceManager.insertRow(database, table, row);
    }

    private void validateRow(String database, String table, Row row) {
        TableSchema tableSchema = databasePersistenceManager.getTableSchema(database, table);
        tableSchema.validateRow(row);
    }

    @Override
    public List<Row> selectAllRows(String database, String table) {
        return databasePersistenceManager.getAllRows(database, table);
    }
}
