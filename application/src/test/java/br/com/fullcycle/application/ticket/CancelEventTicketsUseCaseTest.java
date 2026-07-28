package br.com.fullcycle.application.ticket;

import br.com.fullcycle.application.repository.InMemoryTicketRepository;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.event.ticket.TicketStatus;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CancelEventTicketsUseCaseTest {

    @Test
    @DisplayName("Deve cancelar um ticket de um evento")
    public void testCancelTickets() {
        // given
        final var event = createEvent();
        final var ticket = createTicket(event);
        final var eventId = event.eventId();

        final var createInput = new CancelEventTicketsUseCase.Input(eventId.value());

        // when
        final var ticketRepository = new InMemoryTicketRepository();
        ticketRepository.create(ticket);
        final var useCase = new CancelEventTicketsUseCase(ticketRepository);
        useCase.execute(createInput);

        // then
        final var expectedTicketStatus = TicketStatus.CANCELLED;
        Assertions.assertEquals(expectedTicketStatus, ticket.status());
        final var updatedTicket = ticketRepository.ticketOfId(ticket.ticketId());
        Assertions.assertNotNull(updatedTicket);
        Assertions.assertEquals(expectedTicketStatus, updatedTicket.get().status());
    }

    @Test
    @DisplayName("Deve cancelar todos is tickets de um evento")
    public void testCancelAllTickets() {
        // given
        final var event = createEvent();
        final ArrayList<Ticket> tickets = new ArrayList<Ticket>(List.of(
                                            createTicket(event),
                                            createTicket(event),
                                            createTicket(event)));
        final var eventId = event.eventId();

        final var createInput = new CancelEventTicketsUseCase.Input(eventId.value());

        // when
        final var ticketRepository = new InMemoryTicketRepository();
        tickets.forEach(ticket -> {
            ticketRepository.create(ticket);
        });
        final var useCase = new CancelEventTicketsUseCase(ticketRepository);
        useCase.execute(createInput);

        // then
        final var expectedTicketStatus = TicketStatus.CANCELLED;
        tickets.forEach(ticket -> {
            Assertions.assertEquals(expectedTicketStatus, ticket.status());
        });
        final var updatedTickets = ticketRepository.ticketsByEventId(eventId);
        final var expectedTicketsSize = 3;
        Assertions.assertEquals(expectedTicketsSize, updatedTickets.size());
        updatedTickets.forEach(ticket -> {
            Assertions.assertEquals(expectedTicketStatus, ticket.status());
        });
    }

    Event createEvent() {
        final var aPartner =
            Partner.newPartner("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");

        return Event.newEvent("Disney on Ice", "2021-01-01", 10, aPartner);
    }

    Ticket createTicket(Event anEvent) {

        final var aCustomer =
                Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");

        return Ticket.newTicket(aCustomer.customerId(), anEvent.eventId());
    }
}
