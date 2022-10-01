package ua.knu.csc.it.rdms.port.input;

import java.util.Map;

public record InsertRowCommand(Map<String, Object> nameToValue) {
}
