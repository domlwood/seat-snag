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

    public Long getId() {
        return this.id;
    }

    public Seat getSeat() {
        return this.seat;
    }

    public String getUserId() {
        return this.userId;
    }

    public ReservationStatus getStatus() {
        return this.status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return this.expiresAt;
    }
}
