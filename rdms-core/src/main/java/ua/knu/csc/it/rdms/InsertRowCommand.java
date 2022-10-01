package ua.knu.csc.it.rdms;

import java.util.Map;

public record InsertRowCommand(Map<String, Object> nameToValue) {
}
