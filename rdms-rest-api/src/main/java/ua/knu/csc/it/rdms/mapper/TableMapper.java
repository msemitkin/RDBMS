package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.model.ColumnDto;
import ua.knu.csc.it.rdms.model.CreateColumnDto;
import ua.knu.csc.it.rdms.model.CreateTableDto;
import ua.knu.csc.it.rdms.model.TableDto;
import ua.knu.csc.it.rdms.port.input.CreateTableCommand;
import ua.knu.csc.it.rdms.port.input.TableColumn;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TableMapper {

    private TableMapper() {
    }

    public static TableDto toDto(Table table, TableSchema schema) {
        List<ColumnDto> columns = schema.columns().stream()
            .map(TableMapper::toDto).toList();
        return new TableDto()
            .name(table.name())
            .schema(columns);
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

    private static ColumnDto toDto(@Nonnull Column column) {
        return new ColumnDto()
            .name(column.name())
            .type(column.type().getTypeName());
    }

}
