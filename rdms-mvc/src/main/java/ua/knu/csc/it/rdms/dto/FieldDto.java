package ua.knu.csc.it.rdms.dto;

public class FieldDto {
    private final String name;
    private final Object value;

    public FieldDto(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
