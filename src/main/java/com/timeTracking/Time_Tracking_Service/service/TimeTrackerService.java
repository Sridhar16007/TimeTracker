package com.timeTracking.Time_Tracking_Service.service;

import com.timeTracking.Time_Tracking_Service.model.*;
import com.timeTracking.Time_Tracking_Service.repo.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TimeTrackerService {

    private final TimeTrackerRepo timeTrackerRepo;
    private final ClockLogRepo clockLogRepo;
    private final BreakLogRepo breakLogRepo;

    public TimeTrackerService(TimeTrackerRepo timeTrackerRepo,
                              ClockLogRepo clockLogRepo,
                              BreakLogRepo breakLogRepo) {
        this.timeTrackerRepo = timeTrackerRepo;
        this.clockLogRepo = clockLogRepo;
        this.breakLogRepo = breakLogRepo;
    }

    public TimeTracker toggleClock(long userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withNano(0);

        TimeTracker user = timeTrackerRepo.findById(userId).orElse(null);

        if (user != null && user.isActive() && today.equals(user.getClockInDate())) {
            //  Clock out for the current session
            user.setClock_Out(now);
            user.setClockOutDate(today);

            Duration sessionDuration = Duration.between(user.getClock_in(), user.getClock_Out());
            long sessionMinutes = sessionDuration.toMinutes();

            //  Accumulate total minutes
            user.setTotalMinutes(user.getTotalMinutes() + sessionMinutes);

            long working = user.getTotalMinutes() - user.getBreakTime();
            user.setWorkingMinutes(working);

            //  Set formatted values
            user.setTotalHours(formatDuration(user.getTotalMinutes()));
            user.setWorkingHours(formatDuration(user.getWorkingMinutes()));

            user.setActive(false);
            user.setOnBreak(false);

            // Log the clock out event
            ClockLog clockOutLog = new ClockLog(userId, TimeTrackerConstants.CLOCK_OUT, now, today);
            clockLogRepo.save(clockOutLog);

            return timeTrackerRepo.save(user);

        } else if (user != null && today.equals(user.getClockInDate())) {
            //  Resume session (new clock-in on same day)
            user.setClock_in(now);
            user.setActive(true);

            // Log the clock in event
            ClockLog clockInLog = new ClockLog(userId, TimeTrackerConstants.CLOCK_IN, now, today);
            clockLogRepo.save(clockInLog);

            return timeTrackerRepo.save(user);

        } else {
            //  New day or first-time entry
            if (user == null) user = new TimeTracker();

            user.setUserId(userId);
            user.setClock_in(now);
            user.setClockInDate(today);
            user.setActive(true);

            // Reset all fields for new day
            user.setBreakTime(0);
            user.setTotalMinutes(0);
            user.setWorkingMinutes(0);
            user.setTotalHours("0 hrs 0 mins");
            user.setWorkingHours("0 hrs 0 mins");
            user.setOnBreak(false);

            // Save new user record
            user = timeTrackerRepo.save(user);

            // Log the clock in event
            ClockLog clockInLog = new ClockLog(userId, TimeTrackerConstants.CLOCK_IN, now, today);
            clockLogRepo.save(clockInLog);

            return user;
        }
    }

    public TimeTracker toggleBreak(long userId) {
        TimeTracker user = timeTrackerRepo.findById(userId).orElseThrow(() ->
                new IllegalStateException("User not found with ID: " + userId));

        if (!user.isActive()) {
            throw new IllegalStateException("User is not clocked in.");
        }

        LocalTime now = LocalTime.now().withNano(0);
        LocalDate today = LocalDate.now();

        if (!user.isOnBreak()) {
            // Start break
            user.setBreakStart(now);
            user.setOnBreak(true);

            // Log the break start event
            BreakLog breakStartLog = new BreakLog(userId, TimeTrackerConstants.BREAK_START, now, today);
            breakLogRepo.save(breakStartLog);

        } else {
            // End break and calculate total
            user.setBreakEnd(now);
            Duration breakDuration = Duration.between(user.getBreakStart(), user.getBreakEnd());
            long totalBreak = user.getBreakTime() + breakDuration.toMinutes();
            user.setBreakTime(totalBreak);
            user.setOnBreak(false);

            // Recalculate working minutes
            long working = user.getTotalMinutes() - user.getBreakTime();
            user.setWorkingMinutes(working);
            user.setWorkingHours(formatDuration(user.getWorkingMinutes()));

            // Log the break end event
            BreakLog breakEndLog = new BreakLog(userId, TimeTrackerConstants.BREAK_END, now, today);
            breakLogRepo.save(breakEndLog);
        }

        return timeTrackerRepo.save(user);
    }

    // Get clock logs for a user and date
    public List<ClockLog> getClockLogs(long userId, LocalDate date) {
        return clockLogRepo.findByUserIdAndEventDateOrderByEventTime(userId, date);
    }

    // Get break logs for a user and date
    public List<BreakLog> getBreakLogs(long userId, LocalDate date) {
        return breakLogRepo.findByUserIdAndEventDateOrderByEventTime(userId, date);
    }

    // Get combined daily logs for a user
    public com.timeTracking.Time_Tracking_Service.dto.DailyLogDTO getDailyLogs(long userId, LocalDate date) {
        TimeTracker user = timeTrackerRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        List<ClockLog> clockLogs = getClockLogs(userId, date);
        List<BreakLog> breakLogs = getBreakLogs(userId, date);

        return new com.timeTracking.Time_Tracking_Service.dto.DailyLogDTO(
                userId,
                date,
                user.getTotalHours(),
                user.getWorkingHours(),
                user.getBreakTime(),
                clockLogs,
                breakLogs
        );
    }

    private String formatDuration(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return hours + " hrs " + minutes + " mins";
    }
}