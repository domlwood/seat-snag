package com.seat.snag.seat;

import com.seat.snag.event.Event;
import com.seat.snag.event.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class SeatSeeder {

    private static final Logger log = LoggerFactory.getLogger(SeatSeeder.class);

    @Bean
    CommandLineRunner initDatabase(EventRepository eventRepository, SeatRepository seatRepository) {

        return args -> {
            List<Event> events = eventRepository.findAll();
            log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 2, 2, 2, "coop")));
            log.info("Preloading " + seatRepository.save(new Seat(events.get(1), 2, 2, 2, "coop")));
        };
    }
}