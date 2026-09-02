package com.seat.snag.reservation;

import com.seat.snag.reservation.dto.ReservationRequest;
import com.seat.snag.seat.Seat;
import com.seat.snag.seat.SeatNotFoundException;
import com.seat.snag.seat.SeatRepository;
import com.seat.snag.seat.SeatStatus;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Reservation createReservation(@NonNull ReservationRequest reservationRequest) {
        Long seatId = reservationRequest.seatId();
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new SeatNotFoundException(seatId));

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

        throw new ReservationSeatNotAvailableException(seatId);
    }

    @Transactional
    public void deleteReservation(@NonNull Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));

        Seat seat = reservation.getSeat();
        seat.setStatus(SeatStatus.AVAILABLE);

        reservationRepository.deleteById(id);
    }
}
