package ua.knu.csc.it.rdms;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.port.output.DatabasePersistenceManager;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSystemDatabaseManager implements DatabasePersistenceManager {
    private static final String SCHEMA_EXTENSION = ".schema.json";
    private static final String DATA_EXTENSION = ".data.json";

    private final Path basePath;
    private final ObjectMapper objectMapper;

    public FileSystemDatabaseManager(
        Path basePath,
        ObjectMapper objectMapper
    ) {
        this.basePath = basePath;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveDatabase(@Nonnull String name) {
        try {
            Files.createDirectory(basePath.resolve(name));
        } catch (FileAlreadyExistsException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean existsDatabase(String name) {
        return Files.exists(basePath.resolve(name));
    }

    @Override
    public void saveTable(String databaseName, Table table) {
        Path tableSchemaPath = getTableSchemaPath(databaseName, table.name());
        Path tableDataPath = getTableDataPath(databaseName, table.name());
        try {
            if (Files.notExists(tableSchemaPath)) {
                Files.createFile(tableSchemaPath);
            }
            objectMapper.writeValue(tableSchemaPath.toFile(), new TableSchema(table.columns()));
            if (Files.notExists(tableDataPath)) {
                Files.createFile(tableDataPath);
            }
        } catch (FileAlreadyExistsException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean existsTable(String database, String table) {
        Path tablePath = getTableSchemaPath(database, table);
        return Files.exists(tablePath);
    }

    @Override
    public void insertRow(String database, String table, Row row) {
        Path tablePath = getTableDataPath(database, table);

        RowsDto rows = getRows(database, table);
        ArrayList<RowDto> modifiableList = new ArrayList<>(rows.rows());
        modifiableList.add(RowDto.of(row));

        try {
            objectMapper.writeValue(tablePath.toFile(), new RowsDto(modifiableList));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<Row> getAllRows(String database, String table) {
        RowsDto rowDtos = getRows(database, table);
        return rowDtos.rows()
            .stream()
            .map(RowDto::toRow)
            .toList();
    }

    @Override
    public TableSchema getTableSchema(String database, String table) {
        Path tableSchemaPath = getTableSchemaPath(database, table);
        try {
            return objectMapper.readValue(tableSchemaPath.toFile(), TableSchema.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path getTableSchemaPath(String database, String table) {
        return basePath.resolve(database).resolve(table.concat(SCHEMA_EXTENSION));
    }

    private Path getTableDataPath(String database, String table) {
        return basePath.resolve(database).resolve(table.concat(DATA_EXTENSION));
    }


    private RowsDto getRows(String database, String table) {
        Path tablePath = getTableDataPath(database, table);
        try {
            List<String> lines = Files.readAllLines(tablePath);
            if (lines.isEmpty()) {
                return RowsDto.emptyRow();
            }
            String objectsSource = String.join("", lines);
            objectMapper.readValue(objectsSource, RowsDto.class);
            return objectMapper.readValue(tablePath.toFile(), RowsDto.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
