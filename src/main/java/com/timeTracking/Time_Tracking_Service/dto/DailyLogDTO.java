
package com.timeTracking.Time_Tracking_Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.timeTracking.Time_Tracking_Service.model.BreakLog;
import com.timeTracking.Time_Tracking_Service.model.ClockLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogDTO {
    private long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    private String totalHours;
    private String workingHours;
    private long breakTimeMinutes;

    private List<ClockLog> clockLogs;
    private List<BreakLog> breakLogs;
}