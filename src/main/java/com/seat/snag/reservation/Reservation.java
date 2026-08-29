package com.seat.snag.reservation;

import com.seat.snag.seat.Seat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {
    private @Id
    @GeneratedValue Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    private String userId;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public Reservation() {}

    public Reservation(Seat seat, String userId, ReservationStatus status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.seat = seat;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
