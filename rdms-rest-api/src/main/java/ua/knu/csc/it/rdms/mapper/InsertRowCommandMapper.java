package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.model.InsertDto;
import ua.knu.csc.it.rdms.model.InsertRowColumnValueDto;
import ua.knu.csc.it.rdms.port.input.InsertRowCommand;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class InsertRowCommandMapper {

    private InsertRowCommandMapper() {
    }

    public static InsertRowCommand fromDto(InsertDto insertDto) {
        Map<String, Object> values = insertDto.getValues().stream()
            .collect(toMap(
                InsertRowColumnValueDto::getColumnName,
                InsertRowColumnValueDto::getValue
            ));
        return new InsertRowCommand(values);
    }
}
