package ua.knu.csc.it.rdms.dto;

import java.util.List;

public class TableSchemaDto {
    private final List<ColumnDto> columns;

    public TableSchemaDto(List<ColumnDto> columns) {
        this.columns = columns;
    }

    public List<ColumnDto> getColumns() {
        return columns;
    }
}
