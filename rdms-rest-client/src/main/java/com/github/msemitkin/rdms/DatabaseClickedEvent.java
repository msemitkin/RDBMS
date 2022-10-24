package com.github.msemitkin.rdms;

import javafx.stage.Stage;

public class DatabaseClickedEvent extends ShowDatabasesEvent {
    private final String databaseName;

    public DatabaseClickedEvent(Stage source, String databaseName) {
        super(source);
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
