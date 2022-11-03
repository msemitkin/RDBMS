package com.github.msemitkin.rdms;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ua.knu.csc.it.rdms.api.TablesApi;
import ua.knu.csc.it.rdms.model.ColumnDto;
import ua.knu.csc.it.rdms.model.TableDto;
import ua.knu.csc.it.rdms.model.TablesDto;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class ShowDatabaseController {
    private final TablesApi tablesApi;

    public ShowDatabaseController(TablesApi tablesApi) {
        this.tablesApi = tablesApi;
    }

    @EventListener(ShowDatabaseEvent.class)
    public void showDatabase(ShowDatabaseEvent event) {
        String databaseName = event.getDatabaseName();

        List<TableDto> tables = tablesApi.getTables(databaseName)
            .mapNotNull(TablesDto::getTables)
            .flatMapIterable(Function.identity())
            .collectList()
            .block();

        ObservableList<TableView<ColumnDto>> tableViews = Objects.requireNonNull(tables)
            .stream()
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
