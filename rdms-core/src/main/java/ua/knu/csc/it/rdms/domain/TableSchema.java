package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.Set;

public record TableSchema(Set<Column> columns) {
}
