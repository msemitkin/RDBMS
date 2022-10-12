package ua.knu.csc.it.rdms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.csc.it.rdms.api.TablesApi;
import ua.knu.csc.it.rdms.mapper.TableMapper;
import ua.knu.csc.it.rdms.model.CreateTableDto;
import ua.knu.csc.it.rdms.model.TableDto;
import ua.knu.csc.it.rdms.model.TablesDto;
import ua.knu.csc.it.rdms.port.input.CreateTableCommand;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;

import java.util.List;

@RestController
public class TableController implements TablesApi {
    private final DatabaseManager databaseManager;

    public TableController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public ResponseEntity<Void> createTable(String databaseName, CreateTableDto createTableDto) {
        CreateTableCommand createTableCommand = TableMapper.fromDto(createTableDto);
        databaseManager.createTable(databaseName, createTableCommand);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TablesDto> getTables(String databaseName) {
        List<TableDto> tables = databaseManager.getTables(databaseName).stream()
            .map(TableMapper::toDto)
            .toList();
        return ResponseEntity.ok(new TablesDto().tables(tables));
    }
}
