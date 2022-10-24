package com.github.msemitkin.rdms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ua.knu.csc.it.rdms.api.DatabasesApi;
import ua.knu.csc.it.rdms.model.DatabaseDto;
import ua.knu.csc.it.rdms.model.DatabasesDto;

import java.util.List;
import java.util.function.Function;

@Service
public class DatabasesController {
    private final DatabasesApi databasesApi;

    public DatabasesController(DatabasesApi databasesApi) {
        this.databasesApi = databasesApi;
    }

    @EventListener(ShowDatabasesEvent.class)
    public void showDatabases(ShowDatabasesEvent event) {
        Stage stage = event.getSource();

        ObservableList<String> databases = databasesApi.getDatabases()
            .map(DatabasesDto::getDatabases)
            .flatMapIterable(Function.identity())
            .mapNotNull(DatabaseDto::getName)
            .collect(FXCollections::<String>observableArrayList, List::add)
            .block();

        ListView<String> databasesView = new ListView<>(databases);

        FlowPane root = new FlowPane(databasesView);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Databases");
        stage.show();
    }
}
