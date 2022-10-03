package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.dto.ColumnDto;

public class ColumnMapper {

    private ColumnMapper() {
    }

    public static ColumnDto toDto(Column column) {
        return new ColumnDto(column.type().getTypeName(), column.name());
    }
}
