package com.seat.snag.seat;

public class SeatNotFoundException extends RuntimeException {

    public SeatNotFoundException(Long id) {
        super("Seat Not Found with ID: " + id);
    }
}
