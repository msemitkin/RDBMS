package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.dto.ColumnDto;
import ua.knu.csc.it.rdms.dto.TableSchemaDto;

import java.util.List;

public class TableSchemaMapper {

    private TableSchemaMapper() {
    }

    public static TableSchemaDto toDto(TableSchema tableSchema) {
        List<ColumnDto> columnDtos = tableSchema.columns().stream()
            .map(ColumnMapper::toDto)
            .toList();
        return new TableSchemaDto(columnDtos);
    }
}
