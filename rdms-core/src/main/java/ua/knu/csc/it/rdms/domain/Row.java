package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.Map;

public record Row(Map<Column, Object> values) {
    public Row modify(RowModifier rowModifier) {
        rowModifier.columnModifiers().forEach((columnName, function) -> {
            Column column = getColumnByColumnName(columnName);
            Object currentValue = getValueByColumnName(columnName);
            Object newValue = function.apply(currentValue);
            values.put(column, newValue);
        });
        return new Row(values);
    }

    public Column getColumnByColumnName(String columnName) {
        return values.keySet().stream()
            .filter(o -> o.name().equals(columnName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid column name: %s".formatted(columnName)));
    }

    public Object getValueByColumnName(String columnName) {
        return values.entrySet().stream()
            .filter(columnToValue -> columnToValue.getKey().name().equals(columnName))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid column name: %s".formatted(columnName)));
    }
}
