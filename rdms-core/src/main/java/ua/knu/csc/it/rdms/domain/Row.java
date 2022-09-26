package ua.knu.csc.it.rdms.domain;

import ua.knu.csc.it.rdms.domain.column.Column;

import java.util.Map;

public record Row(Map<Column, Object> values) {
}
