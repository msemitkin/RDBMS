package ua.knu.csc.it.rdms.port.input;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.RowFilter;
import ua.knu.csc.it.rdms.domain.RowModifier;
import ua.knu.csc.it.rdms.domain.column.columntype.ColumnTypes;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;

import javax.annotation.Nonnull;
import java.util.List;

public interface DatabaseManager {

    void createDatabase(@Nonnull String name);

    void createTable(@Nonnull String database, CreateTableCommand createTableCommand);

    void createEnumeration(@Nonnull String database, Enumeration enumeration);

    void dropTable(String database, String table);

    List<Row> selectAllRows(String database, String table);

    void update(String database, String table, RowFilter rowFilter, RowModifier rowModifier);

    void insert(String database, String table, InsertRowCommand row);

    ColumnTypes getSupportedColumnTypes(String database);
}
