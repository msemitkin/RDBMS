package ua.knu.csc.it.rdms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.csc.it.rdms.api.DatabasesApi;
import ua.knu.csc.it.rdms.mapper.DatabaseMapper;
import ua.knu.csc.it.rdms.model.CreateDatabaseDto;
import ua.knu.csc.it.rdms.model.DatabaseDto;
import ua.knu.csc.it.rdms.model.DatabasesDto;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;

import java.util.List;

@RestController
public class DatabaseController implements DatabasesApi {

    private final DatabaseManager databaseManager;

    public DatabaseController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public ResponseEntity<Void> createDatabase(CreateDatabaseDto createDatabaseDto) {
        databaseManager.createDatabase(createDatabaseDto.getName());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<DatabasesDto> getDatabases() {
        List<DatabaseDto> databases = databaseManager.getDatabases()
            .stream().map(DatabaseMapper::toDto).toList();
        return ResponseEntity.ok(new DatabasesDto().databases(databases));
    }

}
