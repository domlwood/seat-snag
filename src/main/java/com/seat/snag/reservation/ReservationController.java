package com.seat.snag.reservation;

import com.seat.snag.reservation.dto.ReservationRequest;
import org.hibernate.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    private final ReservationRepository repository;
    private final ReservationService service;

    public ReservationController(ReservationRepository repository, ReservationService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/reservations")
    List<Reservation> all() {
        return repository.findAll();
    }

    @PostMapping("/reservations")
    Reservation create(@RequestBody ReservationRequest reservationRequest) {
        return service.createReservation(reservationRequest);
    }

    @DeleteMapping("/reservations/{id}")
    void delete(@PathVariable Long id) {
        service.deleteReservation(id);
        return;
    }
}
