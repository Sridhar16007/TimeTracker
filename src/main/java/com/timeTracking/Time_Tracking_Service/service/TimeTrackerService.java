package com.timeTracking.Time_Tracking_Service.service;

import com.timeTracking.Time_Tracking_Service.model.TimeTracker;
import com.timeTracking.Time_Tracking_Service.repo.TimeTrackerRepo;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TimeTrackerService {

    private final TimeTrackerRepo timeTrackerRepo;

    public TimeTrackerService(TimeTrackerRepo timeTrackerRepo) {
        this.timeTrackerRepo = timeTrackerRepo;
    }

    public TimeTracker toggleClock(long id) {
        LocalDate today = LocalDate.now();
        TimeTracker user = timeTrackerRepo.findById(id).orElse(null);

        if (user != null && user.isActive() && today.equals(user.getClockInDate())) {
            // Clock out
            user.setClock_Out(LocalTime.now().withNano(0));
            user.setClockOutDate(today);

            Duration totalDuration = Duration.between(user.getClock_in(), user.getClock_Out());
            long totalMinutes = totalDuration.toMinutes();
            long breakMinutes = user.getBreakTime();
            long workingMinutes = totalMinutes - breakMinutes;

            user.setTotalHours(formatDuration(totalMinutes));
            user.setWorkingHours(formatDuration(workingMinutes));
            user.setActive(false);
            user.setOnBreak(false);

            return timeTrackerRepo.save(user);
        } else {
            // New day or first-time clock-in
            user = new TimeTracker();
            user.setUser_id(id);
            user.setClock_in(LocalTime.now().withNano(0));
            user.setClockInDate(today);
            user.setActive(true);
            user.setBreakTime(0);
            user.setOnBreak(false);

            return timeTrackerRepo.save(user);
        }
    }

    public TimeTracker toggleBreak(long id) {
        TimeTracker user = timeTrackerRepo.findById(id).orElseThrow();

        if (!user.isActive()) {
            throw new IllegalStateException("User is not clocked in.");
        }

        LocalTime now = LocalTime.now().withNano(0);

        if (!user.isOnBreak()) {
            // Start break
            user.setBreakStart(now);
            user.setOnBreak(true);
        } else {
            // End break and calculate duration
            user.setBreakEnd(now);
            Duration breakDuration = Duration.between(user.getBreakStart(), user.getBreakEnd());
            long breakMinutes = user.getBreakTime() + breakDuration.toMinutes();
            user.setBreakTime(breakMinutes);
            user.setOnBreak(false);
        }

        return timeTrackerRepo.save(user);
    }

    private String formatDuration(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return hours + " hrs " + minutes + " mins";
    }
}
