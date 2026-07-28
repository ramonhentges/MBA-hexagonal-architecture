package br.com.fullcycle.application.ticket;

import java.util.Objects;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.event.ticket.TicketRepository;

public class CancelEventTicketsUseCase extends UseCase<CancelEventTicketsUseCase.Input, CancelEventTicketsUseCase.Output> {

    private final TicketRepository ticketRepository;

    public CancelEventTicketsUseCase(final TicketRepository ticketRepository) {
        this.ticketRepository = Objects.requireNonNull(ticketRepository);
    }

    @Override
    public Output execute(final Input input) {

        final var tickets = this.ticketRepository.ticketsByEventId(EventId.with(input.eventId));

        for(Ticket ticket : tickets) {
            ticket.cancel();
            this.ticketRepository.update(ticket);
        }

        return new Output();
    }

    public record Input(String eventId) {}

    public record Output() {}

}
