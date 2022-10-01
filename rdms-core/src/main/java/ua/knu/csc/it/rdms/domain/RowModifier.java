package ua.knu.csc.it.rdms.domain;

import java.util.Map;
import java.util.function.Function;

public record RowModifier(Map<String, Function<Object, Object>> columnModifiers) {
}
