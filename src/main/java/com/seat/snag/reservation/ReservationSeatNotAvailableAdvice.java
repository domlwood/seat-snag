package com.seat.snag.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ReservationSeatNotAvailableAdvice {

    @ExceptionHandler(ReservationSeatNotAvailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String reservationSeatNotAvailableHandler(ReservationSeatNotAvailableException ex) {
        return ex.getMessage();
    }
}