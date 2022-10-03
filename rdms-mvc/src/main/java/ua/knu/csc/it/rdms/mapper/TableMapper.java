package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.dto.TableDto;

public class TableMapper {

    private TableMapper() {
    }

    public static TableDto toDto(Table table) {
        return new TableDto(table.name());
    }
}
