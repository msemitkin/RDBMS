package ua.knu.csc.it.rdms;

import ua.knu.csc.it.rdms.column.OrderDirection;

import java.util.List;

public interface DatabaseManager {

    void createDatabase(String name);

    void createTable(String database, String table);

    void createTable(String database, String table, OrderDirection orderDirection);

    void dropTable(String database, String table);

    List<Row> selectAll(String database, String table);

    void update(String database, String table, Row rowFilter, Row updatedRow);

    void insert(String database, String table, Row row);
}
