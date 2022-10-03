package ua.knu.csc.it.rdms.dto;

public class TableDto {
    private final String name;

    public TableDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
