package br.com.fullcycle.e2e;

import br.com.fullcycle.IntegrationTest;
import br.com.fullcycle.application.event.CancelEventUseCase;
import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.utils.Sleep;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.event.ticket.TicketRepository;
import br.com.fullcycle.domain.event.ticket.TicketStatus;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CancelEventTicketTestIT extends IntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CancelEventUseCase cancelEventUseCase;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        eventRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cancelar os tickets de um evento através da fila")
    public void testCreateCustomer() {
      // given
      final var partner = createPartner("41.536.538/0001-00", "john.doe@gmail.com", "John Doe");
      final var customer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");
      final var event = createEvent("Disney on Ice", "2021-01-01", 100, partner);

      subscribeCustomerToEventUseCase.execute(
        new SubscribeCustomerToEventUseCase.Input(
          customer.customerId().value(),
          event.eventId().value())
        );

      Sleep.sleep(5);

      final var reservedEventTickets = ticketRepository.ticketsByEventId(event.eventId());
      Assertions.assertEquals(1, reservedEventTickets.size());
      final var reservedTicket = reservedEventTickets.get(0);
      Assertions.assertEquals(TicketStatus.PENDING, reservedTicket.status());

      //when
      cancelEventUseCase.execute(new CancelEventUseCase.Input(event.eventId().value()));

      //then
      Sleep.sleep(5);

      final var cancelledEventTickets = ticketRepository.ticketsByEventId(event.eventId());
      Assertions.assertEquals(1, cancelledEventTickets.size());
      final var cancelledTicket = cancelledEventTickets.get(0);
      Assertions.assertEquals(TicketStatus.CANCELLED, cancelledTicket.status());
    }


    private Customer createCustomer(final String cpf, final String email, final String name) {
        return customerRepository.create(Customer.newCustomer(name, cpf, email));
    }

    private Partner createPartner(final String cnpj, final String email, final String name) {
        return partnerRepository.create(Partner.newPartner(name, cnpj, email));
    }

    private Event createEvent(final String name, final String date, final Integer totalSpots, final Partner partner) {
        return eventRepository.create(Event.newEvent(name, date, totalSpots, partner));
    }
}
