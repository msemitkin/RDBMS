package ua.knu.csc.it.rdms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.knu.csc.it.rdms.domain.Database;
import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.dto.DatabaseDto;
import ua.knu.csc.it.rdms.dto.TableDto;
import ua.knu.csc.it.rdms.mapper.DatabaseMapper;
import ua.knu.csc.it.rdms.mapper.TableMapper;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;

import java.util.List;

@Controller
public class DatabaseController {
    private final DatabaseManager databaseManager;

    public DatabaseController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/databases";
    }

    @GetMapping("databases")
    public String getDatabases(Model model) {
        List<Database> databases = databaseManager.getDatabases();
        List<DatabaseDto> databaseDtos = databases.stream()
            .map(DatabaseMapper::toDto)
            .toList();
        model.addAttribute("databases", databaseDtos);
        model.addAttribute("databasesFound", !databaseDtos.isEmpty());
        return "databases";
    }

    @GetMapping("databases/{name}")
    public String getDatabase(@PathVariable("name") String name, Model model) {
        List<Table> tables = databaseManager.getTables(name);
        List<TableDto> tableDtos = tables.stream()
            .map(TableMapper::toDto)
            .toList();
        model.addAttribute("database", name);
        model.addAttribute("tables", tableDtos);
        return "database";
    }

}
