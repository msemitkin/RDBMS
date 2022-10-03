package ua.knu.csc.it.rdms.mapper;

import ua.knu.csc.it.rdms.domain.Database;
import ua.knu.csc.it.rdms.dto.DatabaseDto;

public class DatabaseMapper {

    private DatabaseMapper() {
    }

    public static DatabaseDto toDto(Database database) {
        return new DatabaseDto(database.name());
    }
}
