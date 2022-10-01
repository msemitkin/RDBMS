package ua.knu.csc.it.rdms.domain.column.columntype;

import java.util.regex.Pattern;

public class EmailColumnType extends ColumnType {
    private static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*$^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*$";

    public EmailColumnType() {
        super("EMAIL");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof String stringValue && Pattern.matches(EMAIL_REGEXP, stringValue);
    }

    @Override
    public String toString() {
        return "EmailColumnType{" +
               "name='" + name + '\'' +
               '}';
    }
}
