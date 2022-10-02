package ua.knu.csc.it.rdms.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.domain.column.columntype.IntegerColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.StringColumnType;
import ua.knu.csc.it.rdms.domain.validator.RowValidator;
import ua.knu.csc.it.rdms.port.output.CustomColumnTypePersistenceManager;
import ua.knu.csc.it.rdms.port.output.DatabasePersistenceManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;
import static org.mockito.Mockito.when;

class DatabaseManagerImplTest {

    @Mock
    private DatabasePersistenceManager databasePersistenceManager;
    @Mock
    private CustomColumnTypePersistenceManager customColumnTypePersistenceManager;
    @Mock
    private RowValidator rowValidator;

    private DatabaseManagerImpl databaseManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        databaseManager = new DatabaseManagerImpl(
            databasePersistenceManager,
            customColumnTypePersistenceManager,
            rowValidator
        );
    }

    @Test
    void selectAllRows_shouldReturnSortedRows_whenOrderIsSpecified() {
        String databaseName = "test database";
        String tableName = "test table";

        when(databasePersistenceManager.existsDatabase(databaseName)).thenReturn(true);
        when(databasePersistenceManager.existsTable(databaseName, tableName)).thenReturn(true);
        when(databasePersistenceManager.getTableSchema(databaseName, tableName)).thenReturn(
            new TableSchema(Set.of(
                new Column(new IntegerColumnType(), "id"),
                new Column(new StringColumnType(), "name")
            )));
        when(databasePersistenceManager.getAllRows(databaseName, tableName)).thenReturn(
            List.of(new Row(
                    Map.ofEntries(
                        entry(new Column(new IntegerColumnType(), "id"), 1),
                        entry(new Column(new IntegerColumnType(), "name"), "Forest")
                    )
                ),
                new Row(
                    Map.ofEntries(
                        entry(new Column(new IntegerColumnType(), "id"), 2),
                        entry(new Column(new IntegerColumnType(), "name"), "Anastasia")
                    )
                ),
                new Row(
                    Map.ofEntries(
                        entry(new Column(new IntegerColumnType(), "id"), 3),
                        entry(new Column(new IntegerColumnType(), "name"), "Bob")
                    )
                )
            )
        );

        List<Row> rows = databaseManager
            .selectAllRows(databaseName, tableName, new Sorting("name", SortDirection.DESC));

        Assertions.assertEquals(
            List.of(
                new Row(
                    Map.ofEntries(
                        entry(new Column(new IntegerColumnType(), "id"), 1),
                        entry(new Column(new IntegerColumnType(), "name"), "Forest")
                    )
                ),
                new Row(
                    Map.ofEntries(
                        entry(new Column(new IntegerColumnType(), "id"), 3),
                        entry(new Column(new IntegerColumnType(), "name"), "Bob")
                    )
                ),
                new Row(
                    Map.ofEntries(
                        entry(new Column(new IntegerColumnType(), "id"), 2),
                        entry(new Column(new IntegerColumnType(), "name"), "Anastasia")
                    )
                )
            ),
            rows
        );
    }
}