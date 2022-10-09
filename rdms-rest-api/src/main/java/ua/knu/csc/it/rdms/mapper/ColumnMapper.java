package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.dto.ColumnDto;

import java.util.Collection;
import java.util.List;

public class ColumnMapper {

    private ColumnMapper() {
    }

    public static ColumnDto toDto(Column column) {
        return new ColumnDto(column.name(), column.type().getTypeName());
    }

    public static List<ColumnDto> toDtoList(Collection<Column> columns) {
        return columns.stream().map(ColumnMapper::toDto).toList();
    }
}
