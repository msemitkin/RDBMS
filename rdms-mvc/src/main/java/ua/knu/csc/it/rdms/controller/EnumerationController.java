package ua.knu.csc.it.rdms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;
import ua.knu.csc.it.rdms.port.input.DatabaseManager;

import java.util.HashSet;
import java.util.List;

@Controller
public class EnumerationController {

    private final DatabaseManager databaseManager;

    public EnumerationController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @PostMapping("/databases/{database}/enumerations")
    public String createEnum(
        @PathVariable String database,
        @RequestBody MultiValueMap<String, Object> formData
    ) {
        String name = (String) formData.getFirst("name");
        List<String> values = formData.get("value").stream().map(String.class::cast).toList();
        databaseManager.createEnumeration(database, new Enumeration(name, new HashSet<>(values)));
        return "redirect:/databases/%s".formatted(database);
    }
}
