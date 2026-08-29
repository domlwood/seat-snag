package com.seat.snag.reservation;

import com.seat.snag.reservation.dto.ReservationRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {
    private final ReservationRepository repository;
    private final ReservationService service;

    public ReservationController(ReservationRepository repository, ReservationService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping("/reservations")
    Reservation create(@RequestBody ReservationRequest reservationRequest) {
        return service.createReservation(reservationRequest);
    }
}
