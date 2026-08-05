package br.com.fullcycle.infrastructure.rest.presenters;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.application.event.GetEventByIdUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("publicGetEventById")
public class PublicGetEventById implements Presenter<Optional<GetEventByIdUseCase.Output>, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(PublicGetEventById.class);

    @Override
    public Object present(final Optional<GetEventByIdUseCase.Output> output) {
        return output.map(o -> new PublicGetEventById.Output(o.id(), o.status()))
               .map(ResponseEntity::ok)
               .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public Object present(Throwable error) {
        LOG.error("An error was observer at GetEventByIdUseCase", error);
        return ResponseEntity.notFound().build();
    }

    public record Output(String id, String status) {
    }
}
