package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.Map;

public record Row(Map<Column, Object> values) {
    public Row modify(RowModifier rowModifier) {
        rowModifier.columnModifiers().forEach((column, function) -> {
            Object currentValue = values.get(column);
            Object newValue = function.apply(currentValue);
            values.put(column, newValue);
        });
        return new Row(values);
    }
}
