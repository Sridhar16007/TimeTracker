package com.timeTracking.Time_Tracking_Service.controller;

import com.timeTracking.Time_Tracking_Service.dto.DailyLogDTO;
import com.timeTracking.Time_Tracking_Service.model.BreakLog;
import com.timeTracking.Time_Tracking_Service.model.ClockLog;
import com.timeTracking.Time_Tracking_Service.model.TimeTracker;
import com.timeTracking.Time_Tracking_Service.service.TimeTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("timetracking")
public class TimeTrackerController {

    @Autowired
    private TimeTrackerService timeTrackerService;

    @PostMapping("/clock/{id}")
    public TimeTracker toggleClock(@PathVariable long id) {
        return timeTrackerService.toggleClock(id);
    }

    @PostMapping("/break/{id}")
    public TimeTracker toggleBreak(@PathVariable long id) {
        return timeTrackerService.toggleBreak(id);
    }

    @GetMapping("/clocklogs/{id}")
    public List<ClockLog> getClockLogs(
            @PathVariable long id,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return timeTrackerService.getClockLogs(id, date);
    }

    @GetMapping("/breaklogs/{id}")
    public List<BreakLog> getBreakLogs(
            @PathVariable long id,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return timeTrackerService.getBreakLogs(id, date);
    }

    @GetMapping("/dailylogs/{id}")
    public DailyLogDTO getDailyLogs(
            @PathVariable long id,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return timeTrackerService.getDailyLogs(id, date);
    }
}