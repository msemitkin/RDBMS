package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.dto.CreateColumnDto;
import ua.knu.csc.it.rdms.dto.CreateTableDto;
import ua.knu.csc.it.rdms.port.input.CreateTableCommand;
import ua.knu.csc.it.rdms.port.input.TableColumn;

import java.util.Set;
import java.util.stream.Collectors;

public class TableMapper {

    private TableMapper() {
    }

    public static CreateTableCommand fromDto(CreateTableDto createTableDto) {
        Set<TableColumn> columns = createTableDto.getColumns().stream()
            .map(TableMapper::fromDto)
            .collect(Collectors.toSet());
        return new CreateTableCommand(createTableDto.getName(), columns);
    }

    private static TableColumn fromDto(CreateColumnDto createColumnDto) {
        return new TableColumn(createColumnDto.getType(), createColumnDto.getName());
    }
}
