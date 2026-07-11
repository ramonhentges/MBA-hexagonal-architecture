package br.com.fullcycle.domain.event;

import java.util.Arrays;

import br.com.fullcycle.domain.exceptions.ValidationException;

public class EventStatus {
    private String value;

    public EventStatus(final String status) {
        String[] options = {"OPEN", "CANCELLED"};

        if (status == null || !Arrays.asList(options).contains(status)) {
            throw new ValidationException("Invalid status for EventStatus");
        }
        this.value = status;
    }

    public boolean isCancelled() {
        return value == "CANCELLED";
    }

    public void cancel() {
        if(this.isCancelled()) {
            throw new ValidationException("Event already cancelled");
        }
        this.value = "CANCELLED";
    }

    public String value() {
        return value;
    }

    public static EventStatus create() {
        return new EventStatus("OPEN");
    }

    public static EventStatus with(final String value) {
        try {
            return new EventStatus(value);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid value for EventStatus");
        }
    }
}
