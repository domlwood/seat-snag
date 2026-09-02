package com.seat.snag.reservation;

public class ReservationSeatNotAvailableException extends RuntimeException {

    public ReservationSeatNotAvailableException(Long id) {
        super("Seat " + id + " is not available to reserve");
    }
}
