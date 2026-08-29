package com.seat.snag.event;
import com.seat.snag.seat.Seat;
import com.seat.snag.seat.SeatRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EventController {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    EventController(EventRepository eventRepository, SeatRepository seatRepository) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/events")
    List<Event> all() {
        return eventRepository.findAll();
    }

    @GetMapping("/events/{id}")
    Event findById(@PathVariable Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    @GetMapping("/events/{id}/seats")
    List<Seat> findSeatsById(@PathVariable Long id) {
        return seatRepository.findByEventId(id);
    }
}

