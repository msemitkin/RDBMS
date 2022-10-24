package com.github.msemitkin.rdms;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ua.knu.csc.it.rdms.api.DatabasesApi;
import ua.knu.csc.it.rdms.api.TablesApi;
import ua.knu.csc.it.rdms.model.ColumnDto;
import ua.knu.csc.it.rdms.model.DatabaseDto;
import ua.knu.csc.it.rdms.model.DatabasesDto;
import ua.knu.csc.it.rdms.model.TableDto;
import ua.knu.csc.it.rdms.model.TablesDto;

import java.util.List;
import java.util.function.Function;

@Service
public class DatabasesController {
    private static final Logger logger = LoggerFactory.getLogger(DatabasesController.class);

    private final DatabasesApi databasesApi;
    private final TablesApi tablesApi;
    private final ApplicationEventPublisher applicationEventPublisher;

    public DatabasesController(
        DatabasesApi databasesApi,
        TablesApi tablesApi,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.databasesApi = databasesApi;
        this.tablesApi = tablesApi;
        this.applicationEventPublisher = applicationEventPublisher;
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
                    applicationEventPublisher.publishEvent(new DatabaseClickedEvent(stage, source.getText()));
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

    @EventListener(DatabaseClickedEvent.class)
    public void showDatabase(DatabaseClickedEvent event) {
        String databaseName = event.getDatabaseName();

        List<TableDto> tables = tablesApi.getTables(databaseName)
            .mapNotNull(TablesDto::getTables)
            .flatMapIterable(Function.identity())
            .collectList()
            .block();

        ObservableList<TableView<ColumnDto>> tableViews = tables.stream()
            .map(this::getTableView)
            .collect(FXCollections::observableArrayList, List::add, List::addAll);

        ListView<TableView<ColumnDto>> listView = new ListView<>(tableViews);
        listView.setMaxWidth(Double.MAX_VALUE);
        listView.setMaxHeight(Double.MAX_VALUE);
        FlowPane root = new FlowPane(listView);
        Stage stage = event.getSource();
        stage.getScene().setRoot(root);
        stage.setTitle(databaseName);
        stage.show();
    }

    private TableView<ColumnDto> getTableView(TableDto tableDto) {
        ObservableList<ColumnDto> columns = FXCollections.observableArrayList(tableDto.getSchema());
        TableView<ColumnDto> tableView = new TableView<>(columns);


        TableColumn<ColumnDto, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().add(nameColumn);

        TableColumn<ColumnDto, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableView.getColumns().add(typeColumn);

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty()
            .bind(Bindings.size(tableView.getItems())
                .multiply(tableView.getFixedCellSize()).add(30));
        return tableView;
    }

}
