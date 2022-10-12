package ua.knu.csc.it.rdms.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.knu.csc.it.rdms.domain.DatabaseAlreadyExistsException;
import ua.knu.csc.it.rdms.domain.TableAlreadyExistsException;
import ua.knu.csc.it.rdms.domain.ValidationException;

@RestControllerAdvice
public class RdmsControllerAdvice {

    @ExceptionHandler(DatabaseAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleDatabaseAlreadyExistsException(DatabaseAlreadyExistsException e) {
        return new ErrorDto("Database %s already exists".formatted(e.getDatabaseName()));
    }

    @ExceptionHandler(TableAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleTableAlreadyExistsException(TableAlreadyExistsException e) {
        return new ErrorDto("Table %s already exists".formatted(e.getName()));
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationException(ValidationException e) {
        return new ErrorDto(e.getMessage());
    }
}
