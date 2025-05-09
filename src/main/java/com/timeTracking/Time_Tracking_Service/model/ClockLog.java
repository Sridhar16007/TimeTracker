package com.timeTracking.Time_Tracking_Service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class ClockLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    private String eventType; // "CLOCK_IN" or "CLOCK_OUT"

    private LocalTime eventTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate eventDate;

    public ClockLog(long userId, String eventType, LocalTime eventTime, LocalDate eventDate) {
        this.userId = userId;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
    }
}