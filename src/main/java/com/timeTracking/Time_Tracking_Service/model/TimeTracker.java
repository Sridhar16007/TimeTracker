package com.timeTracking.Time_Tracking_Service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class TimeTracker {

    @Id
    private long user_id;

    private LocalTime clock_in;
    private LocalTime clock_Out;

    private boolean active = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate clockInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate clockOutDate;

    private String totalHours;
    private long breakTime; // in minutes
    private String workingHours;

    private boolean onBreak = false;
    private LocalTime breakStart;
    private LocalTime breakEnd;

    //  Added for internal calculations only
    private long totalMinutes = 0;
    private long workingMinutes = 0;

    public TimeTracker(LocalTime clock_in, LocalTime clock_Out, LocalDate clockInDate, LocalDate clockOutDate,
                       boolean active, String totalHours, long breakTime, String workingHours) {
        this.clock_in = clock_in;
        this.clock_Out = clock_Out;
        this.clockInDate = clockInDate;
        this.clockOutDate = clockOutDate;
        this.active = active;
        this.totalHours = totalHours;
        this.breakTime = breakTime;
        this.workingHours = workingHours;
    }
}
