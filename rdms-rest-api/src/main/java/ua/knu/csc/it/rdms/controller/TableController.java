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
import ua.knu.csc.it.rdms.port.input.InsertRowCommand;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Void> insert(
        String databaseName,
        String tableName,
        Map<String, Object> requestBody
    ) {
        InsertRowCommand insertRowCommand = new InsertRowCommand(requestBody);
        databaseManager.insert(databaseName, tableName, insertRowCommand);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TablesDto> getTables(String databaseName) {
        List<TableDto> tables = databaseManager.getTables(databaseName).stream()
            .map(TableMapper::toDto)
            .toList();
        return ResponseEntity.ok(new TablesDto().tables(tables));
    }

    @Override
    public ResponseEntity<Void> dropTable(String databaseName, String tableName) {
        databaseManager.dropTable(databaseName, tableName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
