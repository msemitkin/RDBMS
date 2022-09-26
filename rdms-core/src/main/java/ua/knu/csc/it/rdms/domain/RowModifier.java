package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.Map;
import java.util.function.Function;

public record RowModifier(Map<Column, Function<Object, Object>> columnModifiers) {
}
