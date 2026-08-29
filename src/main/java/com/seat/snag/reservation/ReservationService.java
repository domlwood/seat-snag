package com.seat.snag.reservation;

import com.seat.snag.reservation.dto.ReservationRequest;
import com.seat.snag.seat.Seat;
import com.seat.snag.seat.SeatRepository;
import com.seat.snag.seat.SeatStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ReservationService {
    final private SeatRepository seatRepository;
    final private ReservationRepository reservationRepository;

    public ReservationService(SeatRepository seatRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this. reservationRepository = reservationRepository;
    }

    public Reservation createReservation(@NonNull ReservationRequest reservationRequest) {
        Long seatId = reservationRequest.seatId();

        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException());

        if(seat.getStatus() == SeatStatus.AVAILABLE) {
            seat.setStatus(SeatStatus.HELD);
             Reservation reservation = new Reservation(
                seat,
                 "DummyUser",
                ReservationStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
            );

            return reservationRepository.save(reservation);
        }

        throw new RuntimeException();
    }
}
