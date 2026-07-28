package br.com.fullcycle.domain.event.ticket;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;

import java.time.Instant;
import java.util.UUID;

public record TicketCancelled(
        String domainEventId,
        String type,
        String ticketId,
        String eventId,
        String customerId,
        Instant occurredOn
) implements DomainEvent {

    public TicketCancelled(TicketId ticketId, EventId eventId, CustomerId customerId) {
        this(UUID.randomUUID().toString(), "ticket.cancelled", ticketId.value(), eventId.value(), customerId.value(), Instant.now());
    }
}
