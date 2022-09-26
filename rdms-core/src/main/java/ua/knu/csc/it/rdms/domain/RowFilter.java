package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.Map;
import java.util.function.Predicate;

public record RowFilter(Map<Column, Predicate<Object>> filters) {

    public boolean matches(Row row) {
        Map<Column, Object> rowValues = row.values();
        return filters.entrySet().stream()
            .allMatch(entry -> {
                Object columnValue = rowValues.get(entry.getKey());
                return entry.getValue().test(columnValue);
            });
    }
}
