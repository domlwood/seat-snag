package com.seat.snag.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventSeeder {

    private static final Logger log = LoggerFactory.getLogger(EventSeeder.class);

//    @Bean
//    CommandLineRunner initDatabase(EventRepository repository) {
//
//        return args -> {
//            log.info("Preloading " + repository.save(new Event("Frodo Baggins 2", "coop", LocalDateTime.now())));
//            log.info("Preloading " + repository.save(new Event("Frodo Baggins", "thief", LocalDateTime.now())));
//        };
//    }
}