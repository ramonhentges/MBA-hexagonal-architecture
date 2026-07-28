package br.com.fullcycle.domain.event.ticket;

import java.util.List;
import java.util.Optional;

import br.com.fullcycle.domain.event.EventId;

public interface TicketRepository {

    Optional<Ticket> ticketOfId(TicketId anId);

    Ticket create(Ticket ticket);

    Ticket update(Ticket ticket);

    List<Ticket> ticketsByEventId(EventId eventId);
    
    void deleteAll();
}
