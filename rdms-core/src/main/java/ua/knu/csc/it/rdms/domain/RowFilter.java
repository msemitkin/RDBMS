package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.Map;
import java.util.function.Predicate;

public record RowFilter(Map<String, Predicate<Object>> nameToFilter) {

    public boolean matches(Row row) {
        Map<Column, Object> rowValues = row.values();
        return nameToFilter.entrySet().stream()
            .allMatch(entry -> {
                String columnName = entry.getKey();
                Object columnValue = getValueByColumnName(rowValues, columnName);
                Predicate<Object> columnFilter = entry.getValue();
                return columnFilter.test(columnValue);
            });
    }

    private Object getValueByColumnName(Map<Column, Object> rowValues, String columnName) {
        return rowValues.entrySet().stream()
            .filter(columnToValue -> columnToValue.getKey().name().equals(columnName))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid column name: %s".formatted(columnName)));
    }

}
