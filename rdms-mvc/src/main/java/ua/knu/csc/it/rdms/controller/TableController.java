package ua.knu.csc.it.rdms.controller;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.SortDirection;
import ua.knu.csc.it.rdms.domain.Sorting;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.domain.column.columntype.CharColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.DoubleColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.EmailColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;
import ua.knu.csc.it.rdms.domain.column.columntype.IntegerColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.StringColumnType;
import ua.knu.csc.it.rdms.dto.RowDto;
import ua.knu.csc.it.rdms.mapper.RowMapper;
import ua.knu.csc.it.rdms.mapper.TableSchemaMapper;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;
import ua.knu.csc.it.rdms.port.input.InsertRowCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

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

    @PostMapping("/databases/{database}/tables/{table}/insert")
    public String insert(
        @PathVariable String database,
        @PathVariable String table,
        @RequestBody MultiValueMap<String, Object> formData
    ) {
        TableSchema tableSchema = databaseManager.getTableSchema(database, table);
        databaseManager.insert(database, table, new InsertRowCommand(castMap(formData, tableSchema)));
        return "redirect:/databases/%s/tables/%s".formatted(database, table);
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

    public Map<String, Object> castMap(MultiValueMap<String, Object> formData, TableSchema tableSchema) {
        Map<String, Object> stringObjectMap = formData.toSingleValueMap();
        return tableSchema.columns().stream()
            .collect(toMap(
                Column::name,
                column -> {
                    Object value = stringObjectMap.get(column.name());
                    return castTo(value, column.type());
                }));
    }

    private Object castTo(Object object, ColumnType to) {
        if (to instanceof IntegerColumnType) {
            return castToInt(object);
        } else if (to instanceof CharColumnType) {
            return castToChar(object);
        } else if (to instanceof DoubleColumnType) {
            return castToDouble(object);
        } else if (to instanceof StringColumnType) {
            return object.toString();
        } else if (to instanceof EmailColumnType) {
            return object.toString();
        } else if (to instanceof Enumeration) {
            return object.toString();
        } else {
            throw new IllegalArgumentException("Not supported type");
        }
    }

    private Object castToChar(Object object) {
        if (object instanceof Character ch) {
            return ch;
        } else if (object.toString().length() == 1) {
            return object.toString().charAt(0);
        } else {
            throw new IllegalArgumentException("Not supported type");
        }
    }

    private Integer castToInt(Object object) {
        try {
            if (object instanceof Integer integer) {
                return integer;
            } else {
                return Integer.parseInt(object.toString());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not supported type");
        }
    }

    private Double castToDouble(Object object) {
        try {
            if (object instanceof Double dbl) {
                return dbl;
            } else {
                return Double.parseDouble(object.toString());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not supported type");
        }
    }


}
