package ua.knu.csc.it.rdms.port.output;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.domain.TableSchema;

import javax.annotation.Nonnull;
import java.util.List;

public interface DatabasePersistenceManager {
    void saveDatabase(@Nonnull String name);

    boolean existsDatabase(String name);

    void saveTable(String databaseName, Table table);

    boolean existsTable(String database, String table);

    TableSchema getTableSchema(String database, String table);

    void insertRow(String database, String table, Row row);

    List<Row> getAllRows(String database, String table);
}
