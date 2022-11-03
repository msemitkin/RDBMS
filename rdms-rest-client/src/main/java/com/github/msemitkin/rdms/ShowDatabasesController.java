package com.github.msemitkin.rdms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ua.knu.csc.it.rdms.api.DatabasesApi;
import ua.knu.csc.it.rdms.model.DatabaseDto;
import ua.knu.csc.it.rdms.model.DatabasesDto;

import java.util.List;
import java.util.function.Function;

@Service
public class ShowDatabasesController {
    private static final Logger logger = LoggerFactory.getLogger(ShowDatabasesController.class);
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DatabasesApi databasesApi;

    public ShowDatabasesController(
        ApplicationEventPublisher applicationEventPublisher,
        DatabasesApi databasesApi
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.databasesApi = databasesApi;
    }

    @EventListener(ShowDatabasesEvent.class)
    public void showDatabases(ShowDatabasesEvent event) {
        Stage stage = event.getSource();
        ObservableList<Text> databases = databasesApi.getDatabases()
            .map(DatabasesDto::getDatabases)
            .flatMapIterable(Function.identity())
            .mapNotNull(DatabaseDto::getName)
            .map(name -> {
                Text text = new Text(name);
                text.setOnMouseClicked(mouseClickEvent -> {
                    Text source = (Text) mouseClickEvent.getSource();
                    String clickedDatabaseName = source.getText();
                    logger.info("{} database clicked", clickedDatabaseName);
                    applicationEventPublisher.publishEvent(new ShowDatabaseEvent(stage, source.getText()));
                });
                return text;
            })
            .collect(FXCollections::<Text>observableArrayList, List::add)
            .block();

        ListView<Text> databasesView = new ListView<>(databases);
        FlowPane root = new FlowPane(databasesView);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Databases");
        stage.show();
    }
}
