package com.timeTracking.Time_Tracking_Service.controller;

import com.timeTracking.Time_Tracking_Service.model.TimeTracker;
import com.timeTracking.Time_Tracking_Service.service.TimeTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
