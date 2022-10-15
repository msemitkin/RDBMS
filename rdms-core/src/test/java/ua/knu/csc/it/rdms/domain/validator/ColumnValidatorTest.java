package ua.knu.csc.it.rdms.domain.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.knu.csc.it.rdms.domain.column.Column;
import ua.knu.csc.it.rdms.domain.column.columntype.ColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.EmailColumnType;
import ua.knu.csc.it.rdms.domain.column.columntype.Enumeration;
import ua.knu.csc.it.rdms.domain.column.columntype.IntegerColumnType;

import java.util.Set;


class ColumnValidatorTest {


    private final ColumnValidator columnValidator = new ColumnValidator();

    @Test
    void validate_shouldNotThrowException_whenValueIsAmongAllowedEnumValues() {
        ColumnType columnType = new Enumeration("WEEKEND_DAYS", Set.of("SATURDAY", "SUNDAY"));
        Column column = new Column(columnType, "test");

        Assertions.assertDoesNotThrow(() -> columnValidator.validate("SATURDAY", column));
    }

    @Test
    void validate_shouldThrowException_whenValueIsNotAmongAllowedEnumValues() {
        ColumnType columnType = new Enumeration("WEEKEND_DAYS", Set.of("SATURDAY", "SUNDAY"));
        Column column = new Column(columnType, "test");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> columnValidator.validate("MONDAY", column)
        );
    }

    @Test
    void validate_shouldThrowException_whenValueHasNotSupportedType() {
        ColumnType columnType = new IntegerColumnType();
        Column column = new Column(columnType, "test");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> columnValidator.validate("Not integer value", column)
        );
    }

    @Test
    void validate_shouldNotThrowException_whenEmailIsValid() {
        ColumnType columnType = new EmailColumnType();
        Column column = new Column(columnType, "test");

        Assertions.assertDoesNotThrow(() -> columnValidator.validate("email@foo.bar", column));
    }

    @Test
    void validate_shouldThrowException_whenEmailIsNotValid() {
        ColumnType columnType = new EmailColumnType();
        Column column = new Column(columnType, "test");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> columnValidator.validate("email@foobar", column)
        );
    }
}