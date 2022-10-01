package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.domain.column.columntype.CharColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.DoubleColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.EmailColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;
import ua.knu.csc.it.rdms.domain.column.columntype.IntegerColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.StringColumnType;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;
import ua.knu.csc.it.rdms.port.input.InsertRowCommand;
import ua.knu.csc.it.rdms.port.output.DatabasePersistenceManager;
import ua.knu.csc.it.rdms.port.output.EnumerationPersistenceManager;
import ua.knu.csc.it.rdms.domain.validator.RowValidator;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class DatabaseManagerImpl implements DatabaseManager {
    private static final Set<ColumnType> PRESET_COLUMN_TYPES = Set.of(
        new IntegerColumnType(),
        new DoubleColumnType(),
        new CharColumnType(),
        new StringColumnType(),
        new EmailColumnType()
    );

    private final DatabasePersistenceManager databasePersistenceManager;
    private final EnumerationPersistenceManager enumerationPersistenceManager;
    private final RowValidator rowValidator;

    public DatabaseManagerImpl(
        DatabasePersistenceManager databasePersistenceManager,
        EnumerationPersistenceManager enumerationPersistenceManager,
        RowValidator rowValidator
    ) {
        this.databasePersistenceManager = databasePersistenceManager;
        this.enumerationPersistenceManager = enumerationPersistenceManager;
        this.rowValidator = rowValidator;
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
    public void createEnumeration(@Nonnull String database, Enumeration enumeration) {
        validateDatabaseExists(database);
        if (databasePersistenceManager.enumerationExists(database, enumeration.getTypeName())) {
            throw new IllegalArgumentException("Enumeration %s already exists".formatted(enumeration.getTypeName()));
        }
        databasePersistenceManager.createEnumeration(database, enumeration);
    }

    @Override
    public void dropTable(String database, String table) {
        validateDatabaseExists(database);
        validateTableExists(database, table);
        databasePersistenceManager.deleteTable(database, table);
    }

    @Override
    public void insert(String database, String table, InsertRowCommand insertRowCommand) {
        validateDatabaseExists(database);
        validateTableExists(database, table);
        TableSchema tableSchema = databasePersistenceManager.getTableSchema(database, table);
        Row row = toRow(insertRowCommand, tableSchema);
        validateRow(database, table, row);
        databasePersistenceManager.insertRow(database, table, row);
    }

    @Override
    public Set<ColumnType> getSupportedColumnTypes(String database) {
        Set<Enumeration> customColumnTypes = enumerationPersistenceManager.getEnumerations(database);
        HashSet<ColumnType> columnTypes = new HashSet<>(PRESET_COLUMN_TYPES);
        columnTypes.addAll(customColumnTypes);
        return columnTypes;
    }

    private void validateRow(String database, String table, Row row) {
        TableSchema tableSchema = databasePersistenceManager.getTableSchema(database, table);
        rowValidator.validateRow(row, tableSchema);
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
        Set<String> columnNames = tableSchema.columns().stream()
            .map(Column::name)
            .collect(Collectors.toSet());
        rowFilter.nameToFilter().keySet()
            .stream()
            .filter(columnName -> !columnNames.contains(columnName))
            .findAny()
            .ifPresent(column -> {
                throw new IllegalArgumentException("Unknown column: %s".formatted(column));
            });
    }

    private void validateRowModifier(RowModifier rowModifier, TableSchema tableSchema) {
        Set<String> columnNames = tableSchema.columns().stream()
            .map(Column::name)
            .collect(Collectors.toSet());
        rowModifier.columnModifiers().keySet()
            .stream()
            .filter(column -> !columnNames.contains(column))
            .findAny()
            .ifPresent(column -> {
                throw new IllegalArgumentException("Unknown column: %s".formatted(column));
            });
    }

    private Row toRow(InsertRowCommand insertRowCommand, TableSchema tableSchema) {
        Map<Column, Object> columns = insertRowCommand.nameToValue().entrySet()
            .stream()
            .collect(toMap(entry -> tableSchema.getByName(entry.getKey()), Map.Entry::getValue));
        return new Row(columns);
    }

}
