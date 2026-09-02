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
    CommandLineRunner seedSeats(EventRepository eventRepository, SeatRepository seatRepository) {
        return args -> {
            if (seatRepository.count() == 0) {
                List<Event> events = eventRepository.findAll();
                log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 2, 2, 2, SeatStatus.AVAILABLE)));
                log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 3, 3, 3, SeatStatus.AVAILABLE)));
                log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 4, 4, 4, SeatStatus.AVAILABLE)));
                log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 5, 5, 5, SeatStatus.AVAILABLE)));
                log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 6, 6, 6, SeatStatus.AVAILABLE)));
                log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 7, 7, 7, SeatStatus.AVAILABLE)));
                log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 8, 8, 8, SeatStatus.AVAILABLE)));
                log.info("Preloading " + seatRepository.save(new Seat(events.get(0), 9, 9, 9, SeatStatus.AVAILABLE)));

                log.info("Preloading " + seatRepository.save(new Seat(events.get(1), 2, 2, 2, SeatStatus.HELD)));
            }
            ;
        };
    }
}