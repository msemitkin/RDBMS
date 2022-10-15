package ua.knu.csc.it.rdms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.knu.csc.it.rdms.api.TablesApi;
import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.RowFilter;
import ua.knu.csc.it.rdms.domain.RowModifier;
import ua.knu.csc.it.rdms.domain.SortDirection;
import ua.knu.csc.it.rdms.domain.Sorting;
import ua.knu.csc.it.rdms.mapper.RowMapper;
import ua.knu.csc.it.rdms.mapper.TableMapper;
import ua.knu.csc.it.rdms.model.CreateTableDto;
import ua.knu.csc.it.rdms.model.RowsDto;
import ua.knu.csc.it.rdms.model.SortingDto;
import ua.knu.csc.it.rdms.model.TableDto;
import ua.knu.csc.it.rdms.model.TablesDto;
import ua.knu.csc.it.rdms.model.UpdateDto;
import ua.knu.csc.it.rdms.port.input.CreateTableCommand;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;
import ua.knu.csc.it.rdms.port.input.InsertRowCommand;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;

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
    public ResponseEntity<Void> update(String databaseName, String tableName, UpdateDto updateDto) {
        Map<String, Object> where = updateDto.getWhere();
        Map<String, Object> set = updateDto.getSet();
        databaseManager.update(
            databaseName,
            tableName,
            extractRowFilter(where),
            extractRowModifier(set)
        );
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
    public ResponseEntity<RowsDto> select(String databaseName, String tableName, SortingDto sorting) {
        SortDirection sortDirection = SortDirection.valueOf(sorting.getSortingOrder().getValue());
        Sorting sortingStrategy = new Sorting(sorting.getSortingColumn(), sortDirection);
        List<Row> rows = databaseManager.selectAllRows(databaseName, tableName, sortingStrategy);

        RowsDto rowsDto = RowMapper.toDtos(rows);
        return ResponseEntity.ok(rowsDto);
    }

    @Override
    public ResponseEntity<Void> dropTable(String databaseName, String tableName) {
        databaseManager.dropTable(databaseName, tableName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private RowFilter extractRowFilter(Map<String, Object> data) {
        Map<String, Predicate<Object>> filter = data.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, entry -> {
                Object value = entry.getValue();
                return o -> o.equals(value);
            }));
        return new RowFilter(filter);
    }

    private RowModifier extractRowModifier(Map<String, Object> data) {
        Map<String, Function<Object, Object>> modifiers = data.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, entry -> (o -> entry.getValue())));
        return new RowModifier(modifiers);
    }
}
