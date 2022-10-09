package ua.knu.csc.it.rdms.dto;

import ua.knu.csc.it.rdms.dto.ColumnDto;

import java.util.List;

public record TableSchemaDto(String name, List<ColumnDto> columns) {
}
