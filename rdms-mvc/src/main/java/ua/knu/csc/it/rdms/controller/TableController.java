package ua.knu.csc.it.rdms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.dto.RowDto;
import ua.knu.csc.it.rdms.mapper.RowMapper;
import ua.knu.csc.it.rdms.mapper.TableSchemaMapper;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;

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
        Model model
    ) {
        //TODO add customizable sorting
        List<Row> rows = databaseManager.selectAllRows(database, table);
        TableSchema tableSchema = databaseManager.getTableSchema(database, table);
        List<RowDto> rowDtos = rows.stream()
            .map(RowMapper::toDto)
            .toList();
        model.addAttribute("databaseName", database);
        model.addAttribute("tableName", table);
        model.addAttribute("columns", TableSchemaMapper.toDto(tableSchema).getColumns());
        model.addAttribute("rows", rowDtos);
        return "table";
    }

    @PostMapping("/databases/{database}/tables/{table}/delete")
    public String deleteTable(@PathVariable String database, @PathVariable String table) {
        databaseManager.dropTable(database, table);
        return "redirect:/databases/%s".formatted(database);
    }
}
