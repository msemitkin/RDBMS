package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.dto.FieldDto;
import ua.knu.csc.it.rdms.dto.RowDto;

import java.util.List;

public class RowMapper {

    private RowMapper() {
    }

    public static RowDto toDto(Row row) {
        List<FieldDto> fields = row.values().entrySet().stream()
            .map(entry -> new FieldDto(entry.getKey().name(), entry.getValue()))
            .toList();
        return new RowDto(fields);
    }
}
