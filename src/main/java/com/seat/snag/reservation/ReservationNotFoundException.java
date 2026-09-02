package com.seat.snag.reservation;

public class ReservationNotFoundException extends RuntimeException {

    ReservationNotFoundException(Long id) {
        super("Reservation not found with ID: " + id);
    }
}
