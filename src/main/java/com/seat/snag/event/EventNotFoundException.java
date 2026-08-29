package com.seat.snag.event;

public class EventNotFoundException extends RuntimeException {

    EventNotFoundException(Long id) {
        super("Could not find event " + id);
    }

}
