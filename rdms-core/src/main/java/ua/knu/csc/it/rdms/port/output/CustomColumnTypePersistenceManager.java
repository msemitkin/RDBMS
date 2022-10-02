package ua.knu.csc.it.rdms.port.output;

import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;

import java.util.Set;

public interface CustomColumnTypePersistenceManager {
    Set<ColumnType> getCustomColumnTypes(String database);
}
