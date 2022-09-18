package ua.knu.csc.it.rdms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class InMemoryDatabaseManagerTest {

    @Test
    void createDatabase_shouldCreateDatabase_whenSpecifiedNameIsAllowed() {
        InMemoryDatabaseManager testManager = new InMemoryDatabaseManager();

        testManager.createDatabase("test");

        Assertions.assertTrue(testManager.hasDatabase("test"));
    }

    @Test
    void createDatabase_shouldThrowException_whenDatabaseWithSuchNameExists() {
        InMemoryDatabaseManager testManager = new InMemoryDatabaseManager();
        testManager.createDatabase("test");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> testManager.createDatabase("test"));
    }

    @Test
    void createTable_shouldCreateTable_whenDatabaseExists() {
        InMemoryDatabaseManager inMemoryDatabaseManager = new InMemoryDatabaseManager();
        inMemoryDatabaseManager.createDatabase("test");

        Assertions.assertDoesNotThrow(() -> inMemoryDatabaseManager.createTable("test", "test table"));
    }

    @Test
    void createTable_shouldThrowException_whenDatabaseDoesNotExist() {
        InMemoryDatabaseManager inMemoryDatabaseManager = new InMemoryDatabaseManager();

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> inMemoryDatabaseManager.createTable("test", "test table"));
    }
}