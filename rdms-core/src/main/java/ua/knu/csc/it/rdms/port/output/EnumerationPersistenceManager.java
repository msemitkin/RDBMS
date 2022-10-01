package ua.knu.csc.it.rdms.port.output;

import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;

import java.util.Set;

public interface EnumerationPersistenceManager {
    Set<Enumeration> getEnumerations(String database);
}
