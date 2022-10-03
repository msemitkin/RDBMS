package ua.knu.csc.it.rdms.dto;

public class ColumnDto {
    private final String type;
    private final String name;

    public ColumnDto(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
