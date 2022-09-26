package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.knu.csc.it.rdms.DatabaseManagerImpl;
import ua.knu.csc.it.rdms.FileSystemDatabaseManager;
import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.RowFilter;
import ua.knu.csc.it.rdms.domain.RowModifier;
import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.domain.column.ColumnType;

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

        DatabaseManagerImpl databaseManager = new DatabaseManagerImpl(
            new FileSystemDatabaseManager(workingDir, new ObjectMapper()));


        String databaseName = "first";
        String tableName = "firsttable";

        databaseManager.createDatabase(databaseName);
        Set<Column> columns = Set.of(
            new Column(ColumnType.INTEGER, "ID"),
            new Column(ColumnType.STRING, "email")
        );
        databaseManager.createTable(databaseName, new Table(tableName, columns));
        databaseManager.insert(databaseName, tableName,
            new Row(Map.ofEntries(
                entry(new Column(ColumnType.INTEGER, "ID"), 10),
                entry(new Column(ColumnType.STRING, "email"), "test")
            )));
        RowFilter filter = new RowFilter(Map.ofEntries(
            entry(new Column(ColumnType.INTEGER, "ID"), value -> (Integer) value > 5)
        ));
        RowModifier modifier = new RowModifier(Map.ofEntries(
            entry(new Column(ColumnType.INTEGER, "ID"), value -> (int) Math.pow((Integer) value, 2))
        ));
        databaseManager.update(databaseName, tableName, filter, modifier);
        databaseManager.selectAllRows(databaseName, tableName).forEach(System.out::println);
    }
}