package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.model.ColumnValueDto;
import ua.knu.csc.it.rdms.model.RowDto;
import ua.knu.csc.it.rdms.model.RowsDto;

import java.util.Collection;
import java.util.List;

public class RowMapper {

    private RowMapper() {
    }

    public static RowDto toDto(Row row) {
        List<ColumnValueDto> columnValues = row.values().entrySet().stream()
            .map(entry -> new ColumnValueDto()
                .columnName(entry.getKey().name())
                .value(entry.getValue()))
            .toList();
        return new RowDto()
            .values(columnValues);
    }

    public static RowsDto toDtos(Collection<Row> rows) {
        return new RowsDto()
            .rows(rows.stream().map(RowMapper::toDto).toList());
    }
}
