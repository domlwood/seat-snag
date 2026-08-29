package com.seat.snag.seat;

import com.seat.snag.event.Event;
import jakarta.persistence.*;

@Entity
public class Seat {
    private @Id
    @GeneratedValue Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private Integer section;
    private Integer row;
    private Integer seatNumber;
    private String status;

    Seat() {}

    public Seat(Event event, Integer section, Integer row, Integer seatNumber, String status) {
        this.event = event;
        this.section = section;
        this.row = row;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public Event getEvent() {
        return this.event;
    }

    public Integer getSection() {
        return this.section;
    }

    public Integer getRow() {
        return this.row;
    }

    public Integer getSeatNumber() {
        return this.seatNumber;
    }

    public String getStatus() {
        return this.status;
    }
}