package ua.knu.csc.it.rdms;

import ua.knu.csc.it.rdms.domain.column.Column;

public record ColumnValueDto(Column column, Object value) {
}
