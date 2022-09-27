package ua.knu.csc.it.rdms;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.RowFilter;
import ua.knu.csc.it.rdms.domain.RowModifier;
import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    void createDatabase(@Nonnull String name);

    void createTable(@Nonnull String database, Table table);

    void createEnumeration(@Nonnull String database, Enumeration enumeration);

    void dropTable(String database, String table);

    List<Row> selectAllRows(String database, String table);

    void update(String database, String table, RowFilter rowFilter, RowModifier rowModifier);

    void insert(String database, String table, Row row);

    Set<ColumnType> getSupportedColumnTypes(String database);
}
