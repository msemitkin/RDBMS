package ua.knu.csc.it.rdms.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import ua.knu.csc.it.rdms.controller.DatabaseController;
import ua.knu.csc.it.rdms.model.CreateDatabaseDto;
import ua.knu.csc.it.rdms.model.DatabasesDto;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DatabasesModelProcessor implements RepresentationModelProcessor<DatabasesDto> {
    @Override
    @Nonnull
    public DatabasesDto process(@Nonnull DatabasesDto model) {
        Link link = linkTo(methodOn(DatabaseController.class).createDatabase(new CreateDatabaseDto()))
            .withSelfRel();
        return model.add(link);
    }
}
