package ua.knu.csc.it.rdms.controller;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.SortDirection;
import ua.knu.csc.it.rdms.domain.Sorting;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.dto.RowDto;
import ua.knu.csc.it.rdms.mapper.RowMapper;
import ua.knu.csc.it.rdms.mapper.TableSchemaMapper;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;

import java.util.Arrays;
import java.util.List;

@Controller
public class TableController {
    private final DatabaseManager databaseManager;

    public TableController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @GetMapping("databases/{database}/tables/{table}")
    public String getTable(
        @PathVariable("database") String database,
        @PathVariable("table") String table,
        @RequestParam(value = "sortingColumn", required = false) String sortingColumn,
        @RequestParam(value = "sortDirection", required = false) String sortDirection,
        Model model
    ) {
        List<Row> rows = getRows(database, table, sortingColumn, sortDirection);
        TableSchema tableSchema = databaseManager.getTableSchema(database, table);
        List<RowDto> rowDtos = rows.stream()
            .map(RowMapper::toDto)
            .toList();
        List<String> columnNames = tableSchema.columns().stream()
            .map(Column::name)
            .toList();
        List<String> sortingDirectionOptions = Arrays.stream(SortDirection.values())
            .map(SortDirection::name)
            .toList();
        model.addAttribute("databaseName", database);
        model.addAttribute("tableName", table);
        model.addAttribute("columns", TableSchemaMapper.toDto(tableSchema).getColumns());
        model.addAttribute("rows", rowDtos);
        model.addAttribute("sortingColumnOptions", columnNames);
        model.addAttribute("sortingDirectionOptions", sortingDirectionOptions);
        return "table";
    }

    @PostMapping("/databases/{database}/tables/{table}/delete")
    public String deleteTable(@PathVariable String database, @PathVariable String table) {
        databaseManager.dropTable(database, table);
        return "redirect:/databases/%s".formatted(database);
    }

    private List<Row> getRows(
        String database,
        String table,
        @Nullable String sortingColumn,
        @Nullable String sortDirection
    ) {
        if (sortDirection != null && sortingColumn != null) {
            Sorting sorting = new Sorting(sortingColumn, SortDirection.valueOf(sortDirection));
            return databaseManager.selectAllRows(database, table, sorting);
        }
        return databaseManager.selectAllRows(database, table);
    }
}
