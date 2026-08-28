package com.seat.snag.event;

import org.springframework.data.jpa.repository.JpaRepository;

interface EventRepository extends JpaRepository<Event, Long> {
}