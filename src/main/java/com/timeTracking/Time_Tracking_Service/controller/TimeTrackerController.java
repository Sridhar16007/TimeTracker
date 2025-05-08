package com.timeTracking.Time_Tracking_Service.controller;

import com.timeTracking.Time_Tracking_Service.model.TimeTracker;
import com.timeTracking.Time_Tracking_Service.service.TimeTrackerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TimeTrackerController {

    private TimeTrackerService timeTrackerService;

    public TimeTrackerController(TimeTrackerService timeTrackerService) {
        this.timeTrackerService = timeTrackerService;
    }

    @PostMapping("/toggle/{id}")
    public ResponseEntity<TimeTracker> toggleTime(@PathVariable long id){
        return new ResponseEntity<TimeTracker>(timeTrackerService.toggle(id), HttpStatus.OK);
    }












//    @PostMapping("clock-in")
//    public ResponseEntity<ClockInResponse> clockIn() {
//        LocalTime clockInTime=timeTrackerService.clockInTime();
//        LocalDate clockInDate=timeTrackerService.clockInDate();
//
//        ClockInResponse clockInResponse=new ClockInResponse(clockInTime,clockInDate);
//        return ResponseEntity.ok(clockInResponse);
//
//    }
//
//    @PostMapping("clock-out")
//    public ResponseEntity<ClockOutResponse> clockOut() {
//
//        LocalTime clockInTime=clockInResponse.getClockInTime();
//        LocalDate clockInDate=clockInResponse.getClockInDate();
//        LocalTime clockOutTime=timeTrackerService.clockOutTime();
//        LocalDate clockOutDate=timeTrackerService.clockOutDate();
//
//        timeTrackerService.totalWorkingHours(clockInDate,clockOutDate,clockInTime,clockOutTime);
//
//        ClockOutResponse clockOutResponse=new ClockOutResponse(clockOutTime,clockOutDate);
//        return ResponseEntity.ok(clockOutResponse);
//    }
}
