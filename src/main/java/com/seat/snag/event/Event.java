package com.seat.snag.event;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Event {
    
  private @Id
  @GeneratedValue Long id;
  private String name;
  private String venue;
  private LocalDateTime startTime;


  Event() {}

  Event(String name, String venue, LocalDateTime startTime) {

    this.name = name;
    this.venue = venue;
    this.startTime = startTime;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getVenue() {
    return this.venue;
  }

  public LocalDateTime getStartTime() {
    return this.startTime;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof Event))
      return false;
    Event event = (Event) o;
    return Objects.equals(this.id, event.id) && Objects.equals(this.name, event.name)
        && Objects.equals(this.venue, event.venue) && Objects.equals(this.startTime, event.startTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.venue, this.startTime);
  }

  @Override
  public String toString() {
    return "Event{" + "id=" + this.id + ", name='" + this.name + '\'' + ", venue='" + this.venue + '\'' + '}';
  }
}