package ua.knu.csc.it.rdms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.csc.it.rdms.domain.Database;
import ua.knu.csc.it.rdms.dto.CreateDatabaseDto;
import ua.knu.csc.it.rdms.dto.DatabaseDto;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;

import java.util.List;

@RestController
public class DatabaseController {

    private final DatabaseManager databaseManager;

    public DatabaseController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @PostMapping("/databases")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDatabase(@RequestBody CreateDatabaseDto createDatabaseDto) {
        databaseManager.createDatabase(createDatabaseDto.getName());
    }

    @GetMapping("/databases")
    public List<DatabaseDto> getDatabases() {
        List<Database> databases = databaseManager.getDatabases();
        return databases.stream().map(this::toDto).toList();
    }

    private DatabaseDto toDto(Database database) {
        return new DatabaseDto(database.name());
    }
}
