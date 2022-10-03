package ua.knu.csc.it.rdms.dto;

public class DatabaseDto {
    private final String name;

    public DatabaseDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
