package ua.knu.csc.it.rdms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.dto.ColumnDto;
import ua.knu.csc.it.rdms.dto.CreateTableDto;
import ua.knu.csc.it.rdms.dto.TableSchemaDto;
import ua.knu.csc.it.rdms.mapper.ColumnMapper;
import ua.knu.csc.it.rdms.mapper.TableMapper;
import ua.knu.csc.it.rdms.port.input.CreateTableCommand;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;

import java.util.List;

@RestController
public class TableController {
    private final DatabaseManager databaseManager;

    public TableController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @PostMapping("/databases/{databaseName}/tables")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTable(@PathVariable String databaseName, @RequestBody CreateTableDto createTableDto) {
        CreateTableCommand createTableCommand = TableMapper.fromDto(createTableDto);
        databaseManager.createTable(databaseName, createTableCommand);
    }

    @GetMapping("/databases/{databaseName}/tables/{tableName}/schema")
    public TableSchemaDto getSchema(@PathVariable String databaseName, @PathVariable String tableName) {
        TableSchema tableSchema = databaseManager.getTableSchema(databaseName, tableName);
        List<ColumnDto> columns = ColumnMapper.toDtoList(tableSchema.columns());
        return new TableSchemaDto(tableName, columns);
    }

}
