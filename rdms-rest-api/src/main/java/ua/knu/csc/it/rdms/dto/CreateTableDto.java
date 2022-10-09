package ua.knu.csc.it.rdms.dto;

import java.util.List;

public class CreateTableDto {
    private String name;
    private List<CreateColumnDto> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CreateColumnDto> getColumns() {
        return columns;
    }

    public void setColumns(List<CreateColumnDto> columns) {
        this.columns = columns;
    }
}
