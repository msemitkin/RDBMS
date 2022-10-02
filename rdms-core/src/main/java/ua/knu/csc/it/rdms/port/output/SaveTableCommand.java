package ua.knu.csc.it.rdms.port.output;

import ua.knu.csc.it.rdms.domain.column.Column;

import javax.annotation.Nonnull;
import java.util.Set;

public record SaveTableCommand(@Nonnull String name, Set<Column> columns) {
}
