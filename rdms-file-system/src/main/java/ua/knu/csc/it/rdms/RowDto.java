package ua.knu.csc.it.rdms;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RowDto(List<ColumnValueDto> values) {

    public static RowDto of(Row row) {
        List<ColumnValueDto> columnValueDtos = row.values().entrySet().stream()
            .map(entry -> new ColumnValueDto(entry.getKey(), entry.getValue()))
            .toList();
        return new RowDto(columnValueDtos);
    }

    public Row toRow() {
        Map<Column, Object> row = values.stream()
            .collect(Collectors.toMap(ColumnValueDto::column, ColumnValueDto::value));
        return new Row(row);
    }
}
