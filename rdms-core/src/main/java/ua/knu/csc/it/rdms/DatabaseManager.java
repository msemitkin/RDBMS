package ua.knu.csc.it.rdms;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.Table;

import javax.annotation.Nonnull;
import java.util.List;

public interface DatabaseManager {

    void createDatabase(@Nonnull String name);

    void createTable(@Nonnull String database, Table table);

    void dropTable(String database, String table);

    List<Row> selectAllRows(String database, String table);

//    void update(String database, String table, Row rowFilter, Row updatedRow);

    void insert(String database, String table, Row row);
}
