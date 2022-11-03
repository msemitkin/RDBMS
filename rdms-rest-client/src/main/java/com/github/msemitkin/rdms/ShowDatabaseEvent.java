package com.github.msemitkin.rdms;

import javafx.stage.Stage;

public class ShowDatabaseEvent extends ShowStageEvent {
    private final String databaseName;

    public ShowDatabaseEvent(Stage source, String databaseName) {
        super(source);
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
