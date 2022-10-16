package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.Set;

public record TableSchema(Set<Column> columns) {
    public Column getByName(String name) {
        return columns.stream()
            .filter(column -> column.name().equals(name))
            .findAny()
            .orElseThrow(() -> new UnknownColumnException("Invalid column name: %s".formatted(name)));
    }
}
