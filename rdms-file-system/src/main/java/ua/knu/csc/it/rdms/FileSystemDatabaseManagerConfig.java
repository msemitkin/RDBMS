package ua.knu.csc.it.rdms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Configuration
public class FileSystemDatabaseManagerConfig {

    //    @Bean
    public FileSystemDatabaseManager fileSystemDatabaseManager() throws IOException {
        Path home = Path.of(System.getProperty("user.home"));
        Path workingDir = home.resolve("test_" + LocalDateTime.now());
        Files.createDirectory(workingDir);

        return new FileSystemDatabaseManager(workingDir, objectMapper());
    }

    @Bean
    public FileSystemDatabaseManager fileSystemDatabaseManager(
        @Value("rdms_test") String pathFromHomeDirectory
    ) {
        Path home = Path.of(System.getProperty("user.home"));
        Path workingDir = home.resolve(pathFromHomeDirectory);

        return new FileSystemDatabaseManager(workingDir, objectMapper());
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType("ua.knu.csc.it.rdms.domain.column.columntype")
            .allowIfSubType("java.util")
            .build();
        objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
}
