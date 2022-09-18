package ua.knu.csc.it.rdms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class DatabaseManagerTest {

    @Test
    void createDatabase_shouldCreateDatabase_whenSpecifiedNameIsAllowed() {
        DatabaseManager testManager = new DatabaseManager();

        testManager.createDatabase("test");

        Assertions.assertTrue(testManager.hasDatabase("test"));
    }

    @Test
    void createDatabase_shouldThrowException_whenDatabaseWithSuchNameExists() {
        DatabaseManager testManager = new DatabaseManager();
        testManager.createDatabase("test");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> testManager.createDatabase("test"));
    }

    @Test
    void createTable_shouldCreateTable_whenDatabaseExists() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.createDatabase("test");

        Assertions.assertDoesNotThrow(() -> databaseManager.createTable("test", "test table"));
    }

    @Test
    void createTable_shouldThrowException_whenDatabaseDoesNotExist() {
        DatabaseManager databaseManager = new DatabaseManager();

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> databaseManager.createTable("test", "test table"));
    }
}