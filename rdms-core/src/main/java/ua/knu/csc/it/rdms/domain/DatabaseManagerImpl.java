package ua.knu.csc.it.rdms.domain;

import org.springframework.stereotype.Service;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.domain.column.columntype.CharColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.ColumnTypes;
import ua.knu.csc.it.rdms.domain.column.columntype.DoubleColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.EmailColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;
import ua.knu.csc.it.rdms.domain.column.columntype.IntegerColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.StringColumnType;
import ua.knu.csc.it.rdms.domain.validator.RowValidator;
import ua.knu.csc.it.rdms.port.input.CreateTableCommand;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;
import ua.knu.csc.it.rdms.port.input.InsertRowCommand;
import ua.knu.csc.it.rdms.port.output.CustomColumnTypePersistenceManager;
import ua.knu.csc.it.rdms.port.output.DatabasePersistenceManager;
import ua.knu.csc.it.rdms.port.output.SaveTableCommand;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class DatabaseManagerImpl implements DatabaseManager {
    private static final Set<ColumnType> PRESET_COLUMN_TYPES = Set.of(
        new IntegerColumnType(),
        new DoubleColumnType(),
        new CharColumnType(),
        new StringColumnType(),
        new EmailColumnType()
    );

    private final DatabasePersistenceManager databasePersistenceManager;
    private final CustomColumnTypePersistenceManager customColumnTypePersistenceManager;
    private final RowValidator rowValidator;

    public DatabaseManagerImpl(
        DatabasePersistenceManager databasePersistenceManager,
        CustomColumnTypePersistenceManager customColumnTypePersistenceManager,
        RowValidator rowValidator
    ) {
        this.databasePersistenceManager = databasePersistenceManager;
        this.customColumnTypePersistenceManager = customColumnTypePersistenceManager;
        this.rowValidator = rowValidator;
    }

    @Override
    public List<Database> getDatabases() {
        return databasePersistenceManager.getDatabases();
    }

    @Override
    public void createDatabase(@Nonnull String name) {
        validateDatabaseDoesNotExist(name);
        databasePersistenceManager.saveDatabase(name);
    }

    @Override
    public void createTable(@Nonnull String database, CreateTableCommand createTableCommand) {
        validateDatabaseExists(database);
        validateTableDoesNotExist(database, createTableCommand);
        ColumnTypes supportedColumnTypes = getSupportedColumnTypes(database);
        Set<Column> columns = getColumns(createTableCommand, supportedColumnTypes);
        databasePersistenceManager.saveTable(
            database,
            new SaveTableCommand(createTableCommand.name(), columns)
        );
    }

    @Override
    public List<Table> getTables(String database) {
        return databasePersistenceManager.getTables(database);
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
    public TableSchema getTableSchema(String database, String table) {
        return databasePersistenceManager.getTableSchema(database, table);
    }

    @Override
    public List<Row> selectAllRows(String database, String table) {
        return databasePersistenceManager.getAllRows(database, table);
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
    public ColumnTypes getSupportedColumnTypes(String database) {
        Set<ColumnType> customColumnTypes = customColumnTypePersistenceManager.getCustomColumnTypes(database);
        HashSet<ColumnType> columnTypes = new HashSet<>(PRESET_COLUMN_TYPES);
        columnTypes.addAll(customColumnTypes);
        return new ColumnTypes(columnTypes);
    }

    private void validateRow(String database, String table, Row row) {
        TableSchema tableSchema = databasePersistenceManager.getTableSchema(database, table);
        rowValidator.validateRow(row, tableSchema);
    }

    @Override
    public List<Row> selectAllRows(String database, String table, Sorting sorting) {
        validateDatabaseExists(database);
        validateTableExists(database, table);
        List<Row> rows = databasePersistenceManager.getAllRows(database, table);
        Column sortingColumn = databasePersistenceManager.getTableSchema(database, table)
            .getByName(sorting.column());
        Comparator<Row> rowComparator = getRowComparator(sortingColumn);
        return rows.stream()
            .sorted(sorting.direction() == SortDirection.ASC ? rowComparator : rowComparator.reversed())
            .toList();
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
        databasePersistenceManager.saveTable(database, new SaveTableCommand(table, tableSchema.columns()));
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
            throw new IllegalArgumentException("SaveTableCommand %s does not exist".formatted(table));
        }
    }

    private void validateTableDoesNotExist(String database, CreateTableCommand createTableCommand) {
        if (databasePersistenceManager.existsTable(database, createTableCommand.name())) {
            throw new IllegalArgumentException("SaveTableCommand %s already exists"
                .formatted(createTableCommand.name()));
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

    private Set<Column> getColumns(
        CreateTableCommand createTableCommand,
        ColumnTypes supportedColumnTypes
    ) {
        return createTableCommand.tableColumns().stream()
            .map(column -> {
                String columnName = column.name();
                ColumnType columnType = supportedColumnTypes.getByName(column.type());
                return new Column(columnType, columnName);
            })
            .collect(Collectors.toSet());
    }

    private Row toRow(InsertRowCommand insertRowCommand, TableSchema tableSchema) {
        Map<Column, Object> columns = insertRowCommand.nameToValue().entrySet()
            .stream()
            .collect(toMap(entry -> tableSchema.getByName(entry.getKey()), Map.Entry::getValue));
        return new Row(columns);
    }

    private Comparator<Row> getRowComparator(Column sortingColumn) {
        return (o1, o2) -> {
            Comparator<Object> columnComparator = sortingColumn.type().getComparator();
            Object value1 = o1.getValueByColumnName(sortingColumn.name());
            Object value2 = o2.getValueByColumnName(sortingColumn.name());
            return columnComparator.compare(value1, value2);
        };
    }

}
