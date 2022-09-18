package ua.knu.csc.it.rdms;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Table {
    private final String name;

    public Table(@Nonnull String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }
}

//TODO add table sorting
