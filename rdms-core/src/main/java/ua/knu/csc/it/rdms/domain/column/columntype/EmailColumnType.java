package ua.knu.csc.it.rdms.domain.column.columntype;

import java.util.regex.Pattern;

public class EmailColumnType extends ColumnType {
    private static final Pattern EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public EmailColumnType() {
        super("EMAIL");
    }

    @Override
    public boolean isValid(Object value) {
        return value instanceof String stringValue && EMAIL_ADDRESS_REGEX.matcher(stringValue).find();
    }

    @Override
    public String toString() {
        return "EmailColumnType{" +
               "name='" + typeName + '\'' +
               '}';
    }
}
