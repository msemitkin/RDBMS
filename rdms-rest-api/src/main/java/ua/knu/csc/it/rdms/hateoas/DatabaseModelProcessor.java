package ua.knu.csc.it.rdms.hateoas;

import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import ua.knu.csc.it.rdms.controller.TableController;
import ua.knu.csc.it.rdms.model.DatabaseDto;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DatabaseModelProcessor implements RepresentationModelProcessor<DatabaseDto> {
    @Override
    @Nonnull
    public DatabaseDto process(@Nonnull DatabaseDto model) {
        String databaseName = model.getName();
        model.add(linkTo(methodOn(TableController.class).getTables(databaseName)).withRel("tables"));
        return model;
    }
}
