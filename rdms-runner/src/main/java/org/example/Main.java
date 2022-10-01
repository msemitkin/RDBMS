package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.knu.csc.it.rdms.FileSystemDatabaseManager;
import ua.knu.csc.it.rdms.domain.DatabaseManagerImpl;
import ua.knu.csc.it.rdms.domain.RowFilter;
import ua.knu.csc.it.rdms.domain.RowModifier;
import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.domain.column.columntype.EmailColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;
import ua.knu.csc.it.rdms.domain.column.columntype.IntegerColumnType;
import ua.knu.csc.it.rdms.domain.validator.ColumnValidator;
import ua.knu.csc.it.rdms.domain.validator.RowValidator;
import ua.knu.csc.it.rdms.port.input.InsertRowCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class Main {
    public static void main(String[] args) throws IOException {
        Path home = Path.of(System.getProperty("user.home"));
        Path workingDir = home.resolve("test_" + LocalDateTime.now());
        Files.createDirectory(workingDir);

        FileSystemDatabaseManager databasePersistenceManager =
            new FileSystemDatabaseManager(workingDir, new ObjectMapper());
        DatabaseManagerImpl databaseManager = new DatabaseManagerImpl(
            databasePersistenceManager,
            databasePersistenceManager,
            new RowValidator(new ColumnValidator())
        );


        String databaseName = "first";
        String tableName = "firsttable";

        databaseManager.createDatabase(databaseName);
        Set<Column> columns = Set.of(
            new Column(new IntegerColumnType(), "ID"),
            new Column(new EmailColumnType(), "email")
        );
        databaseManager.createTable(databaseName, new Table(tableName, columns));
        databaseManager.insert(databaseName, tableName,
            new InsertRowCommand(Map.ofEntries(
                entry("ID", 10),
                entry("email", "test@gmail.com")
            )));
        RowFilter filter = new RowFilter(Map.ofEntries(
            entry("ID", value -> (Integer) value > 5)
        ));
        RowModifier modifier = new RowModifier(Map.ofEntries(
            entry("ID", value -> (int) Math.pow((Integer) value, 2))
        ));
        databaseManager.update(databaseName, tableName, filter, modifier);
        databaseManager.selectAllRows(databaseName, tableName).forEach(System.out::println);

        databaseManager.createEnumeration(databaseName, new Enumeration("booleanValues", Set.of("TRUE", "FALSE")));
    }
}