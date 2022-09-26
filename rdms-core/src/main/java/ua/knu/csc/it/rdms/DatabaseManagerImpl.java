package ua.knu.csc.it.rdms;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.RowFilter;
import ua.knu.csc.it.rdms.domain.RowModifier;
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
        validateDatabaseDoesNotExist(name);
        databasePersistenceManager.saveDatabase(name);
    }

    @Override
    public void createTable(@Nonnull String database, Table table) {
        validateDatabaseExists(database);
        validateTableDoesNotExist(database, table);
        databasePersistenceManager.saveTable(database, table);
    }

    @Override
    public void dropTable(String database, String table) {
        validateDatabaseExists(database);
        validateTableExists(database, table);
        databasePersistenceManager.deleteTable(database, table);
    }

    @Override
    public void insert(String database, String table, Row row) {
        validateDatabaseExists(database);
        validateTableExists(database, table);
        validateRow(database, table, row);
        databasePersistenceManager.insertRow(database, table, row);
    }

    private void validateRow(String database, String table, Row row) {
        TableSchema tableSchema = databasePersistenceManager.getTableSchema(database, table);
        tableSchema.validateRow(row);
    }

    @Override
    public List<Row> selectAllRows(String database, String table) {
        validateDatabaseExists(database);
        validateTableExists(database, table);
        return databasePersistenceManager.getAllRows(database, table);
    }

    @Override
    public void update(String database, String table, RowFilter rowFilter, RowModifier rowModifier) {
        validateDatabaseExists(database);
        validateTableExists(database, table);
        TableSchema tableSchema = databasePersistenceManager.getTableSchema(database, table);
        validateCriteria(rowFilter, tableSchema);
        validateRowModifier(rowModifier, tableSchema);
        List<Row> rows = databasePersistenceManager.getAllRows(database, table);
        List<Row> updatedRows = rows.stream()
            .map(row -> rowFilter.matches(row) ? row.modify(rowModifier) : row)
            .toList();
        databasePersistenceManager.deleteTable(database, table);
        databasePersistenceManager.saveTable(database, new Table(table, tableSchema.columns()));
        updatedRows.forEach(row -> databasePersistenceManager.insertRow(database, table, row));
    }

    private void validateDatabaseExists(String database) {
        if (!databasePersistenceManager.existsDatabase(database)) {
            throw new IllegalArgumentException("Database %s does not exist".formatted(database));
        }
    }

    private void validateDatabaseDoesNotExist(String name) {
        if (databasePersistenceManager.existsDatabase(name)) {
            throw new IllegalArgumentException("Database %s already exists".formatted(name));
        }
    }

    private void validateTableExists(String database, String table) {
        if (!databasePersistenceManager.existsTable(database, table)) {
            throw new IllegalArgumentException("Table %s does not exist".formatted(table));
        }
    }

    private void validateTableDoesNotExist(String database, Table table) {
        if (databasePersistenceManager.existsTable(database, table.name())) {
            throw new IllegalArgumentException("Table %s already exists".formatted(table.name()));
        }
    }

    private void validateCriteria(RowFilter rowFilter, TableSchema tableSchema) {
        rowFilter.filters().keySet()
            .stream()
            .filter(column -> !tableSchema.columns().contains(column))
            .findAny()
            .ifPresent(column -> {
                throw new IllegalArgumentException("Unknown column: %s".formatted(column));
            });
    }

    private void validateRowModifier(RowModifier rowModifier, TableSchema tableSchema) {
        rowModifier.columnModifiers().keySet()
            .stream()
            .filter(column -> !tableSchema.columns().contains(column))
            .findAny()
            .ifPresent(column -> {
                throw new IllegalArgumentException("Unknown column: %s".formatted(column));
            });
    }

}
