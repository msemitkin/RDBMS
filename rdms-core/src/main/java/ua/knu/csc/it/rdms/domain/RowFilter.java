package ua.knu.csc.it.rdms.domain;

import java.util.Map;
import java.util.function.Predicate;

public record RowFilter(Map<String, Predicate<Object>> nameToFilter) {

    public boolean matches(Row row) {
        return nameToFilter.entrySet().stream()
            .allMatch(entry -> {
                String columnName = entry.getKey();
                Object columnValue = row.getValueByColumnName(columnName);
                Predicate<Object> columnFilter = entry.getValue();
                return columnFilter.test(columnValue);
            });
    }

}
