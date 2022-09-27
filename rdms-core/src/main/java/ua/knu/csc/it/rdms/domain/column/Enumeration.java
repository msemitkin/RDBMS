package ua.knu.csc.it.rdms.domain.column;

import java.util.Set;

public record Enumeration(String name, Set<String> values) {
}
