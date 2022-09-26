package ua.knu.csc.it.rdms;

import java.util.List;

import static java.util.Collections.emptyList;

public record RowsDto(List<RowDto> rows) {

    public static RowsDto emptyRow() {
        return new RowsDto(emptyList());
    }

}
