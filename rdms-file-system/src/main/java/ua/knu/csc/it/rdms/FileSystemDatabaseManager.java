package ua.knu.csc.it.rdms;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.knu.csc.it.rdms.domain.Row;
import ua.knu.csc.it.rdms.domain.Table;
import ua.knu.csc.it.rdms.domain.TableSchema;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;
import ua.knu.csc.it.rdms.port.output.DatabasePersistenceManager;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemDatabaseManager implements DatabasePersistenceManager, EnumerationPersistenceManager {
    private static final String SCHEMA_EXTENSION = ".table.schema.json";
    private static final String DATA_EXTENSION = ".table.data.json";
    private static final String ENUM_EXTENSION = ".enum.json";

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
    public void deleteTable(String database, String table) {
        try {
            Path tableDataPath = getTableDataPath(database, table);
            Files.delete(tableDataPath);
            Path tableSchemaPath = getTableSchemaPath(database, table);
            Files.delete(tableSchemaPath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean enumerationExists(String database, String name) {
        Path enumPath = getEnumPath(database, name);
        return Files.exists(enumPath);
    }

    @Override
    public void createEnumeration(String database, Enumeration enumeration) {
        Path enumPath = getEnumPath(database, enumeration.getTypeName());
        try {
            objectMapper.writeValue(enumPath.toFile(), enumeration);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Set<Enumeration> getEnumerations(String database) {
        try (Stream<Path> pathStream = Files
                .find(basePath.resolve(database), 1, (path, attributes) -> path.endsWith(ENUM_EXTENSION))
        ) {
            return pathStream
                .map(path -> readValue(path, Enumeration.class))
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

    private Path getEnumPath(String database, String enumName) {
        return basePath.resolve(database).resolve(enumName.concat(ENUM_EXTENSION));
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

    private <T> T readValue(Path path, Class<T> type) {
        try {
            return objectMapper.readValue(path.toFile(), type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
