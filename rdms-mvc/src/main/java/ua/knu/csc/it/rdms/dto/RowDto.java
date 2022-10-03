package ua.knu.csc.it.rdms.dto;

import java.util.List;

public class RowDto {
    private final List<FieldDto> fields;

    public RowDto(List<FieldDto> fields) {
        this.fields = fields;
    }

    public List<FieldDto> getFields() {
        return fields;
    }
}
