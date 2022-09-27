package ua.knu.csc.it.rdms.domain.column;

import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;

public record Column(ColumnType type, String name) {
}
