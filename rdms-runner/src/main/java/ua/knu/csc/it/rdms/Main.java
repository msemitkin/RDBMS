package ua.knu.csc.it.rdms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.knu.csc.it.rdms.domain.RowFilter;
import ua.knu.csc.it.rdms.domain.RowModifier;
import ua.knu.csc.it.rdms.domain.SortDirection;
import ua.knu.csc.it.rdms.domain.Sorting;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;
import ua.knu.csc.it.rdms.port.input.CreateTableCommand;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;
import ua.knu.csc.it.rdms.port.input.InsertRowCommand;
import ua.knu.csc.it.rdms.port.input.TableColumn;

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;


@SpringBootApplication
public class Main implements CommandLineRunner {
    private final DatabaseManager databaseManager;

    public Main(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        String databaseName = "first";
        String tableName = "firsttable";

        databaseManager.createDatabase(databaseName);

        databaseManager.createEnumeration(
            databaseName,
            new Enumeration("BOOLEANVALUE", Set.of("TRUE", "FALSE"))
        );

        Set<TableColumn> tableColumns = Set.of(
            new TableColumn("INTEGER", "ID"),
            new TableColumn("EMAIL", "email"),
            new TableColumn("BOOLEANVALUE", "true_or_false")
        );
        databaseManager.createTable(databaseName, new CreateTableCommand(tableName, tableColumns));

        databaseManager.insert(databaseName, tableName,
            new InsertRowCommand(Map.ofEntries(
                entry("ID", 10),
                entry("email", "test@gmail.com"),
                entry("true_or_false", "TRUE")
            ))
        );
        databaseManager.insert(databaseName, tableName,
            new InsertRowCommand(Map.ofEntries(
                entry("ID", 30),
                entry("email", "test@gmail.com"),
                entry("true_or_false", "TRUE")
            ))
        );
        databaseManager.insert(databaseName, tableName,
            new InsertRowCommand(Map.ofEntries(
                entry("ID", 20),
                entry("email", "test@gmail.com"),
                entry("true_or_false", "TRUE")
            ))
        );

        RowFilter filter = new RowFilter(Map.ofEntries(
            entry("ID", value -> (Integer) value > 5)
        ));
        RowModifier modifier = new RowModifier(Map.ofEntries(
            entry("ID", value -> (int) Math.pow((Integer) value, 2))
        ));
        databaseManager.update(databaseName, tableName, filter, modifier);
        databaseManager.selectAllRows(databaseName, tableName, new Sorting("ID", SortDirection.DESC))
            .forEach(System.out::println);
    }
}