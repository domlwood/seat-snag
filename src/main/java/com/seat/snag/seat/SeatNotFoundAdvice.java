package com.seat.snag.seat;

import com.seat.snag.reservation.ReservationSeatNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SeatNotFoundAdvice {

    @ExceptionHandler(SeatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String seatNotFoundHandler(SeatNotFoundException ex) {
        return ex.getMessage();
    }
}
