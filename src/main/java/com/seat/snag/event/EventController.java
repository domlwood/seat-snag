package com.seat.snag.event;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    private final EventRepository repository;

    EventController(EventRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/events")
    List<Event> all() {
        return repository.findAll();
    }
}
