package br.com.fullcycle.application.event;

import br.com.fullcycle.application.repository.InMemoryEventRepository;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetEventByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um evento pelo id")
    public void testGetEventById() throws Exception {
        // given
        final var aPartner = Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var expectedName = "Disney on Ice";
        final var expectedDate = "2021-01-01";
        final var expectedTotalSpots = 10;

        final var anEvent = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, aPartner);

        final var expectedId = anEvent.eventId().value();

        final var getEventByIdInput =
                new GetEventByIdUseCase.Input(expectedId);

        final var eventRepository = new InMemoryEventRepository();

        eventRepository.create(anEvent);

        // when
        final var useCase = new GetEventByIdUseCase(eventRepository);
        final var output = useCase.execute(getEventByIdInput).get();

        // then
        final var expectedStatus = "OPEN";
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDate, output.date());
        Assertions.assertEquals(expectedTotalSpots, output.totalSpots());
        Assertions.assertEquals(expectedStatus, output.status());
    }

    @Test
    @DisplayName("Deve obter vazio para um evento que não existe")
    public void testGetEventThatNotExists() throws Exception {
        // given
        final var eventID = EventId.unique().value();

        final var getEventByIdInput =
                new GetEventByIdUseCase.Input(eventID);

        final var eventRepository = new InMemoryEventRepository();

        // when
        final var useCase = new GetEventByIdUseCase(eventRepository);
        final var output = useCase.execute(getEventByIdInput);

        // then
        Assertions.assertTrue(output.isEmpty());
    }
}