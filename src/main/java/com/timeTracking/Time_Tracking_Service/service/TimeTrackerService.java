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
            //  Clock out for the current session
            user.setClock_Out(LocalTime.now().withNano(0));
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

            return timeTrackerRepo.save(user);

        } else if (user != null && today.equals(user.getClockInDate())) {
            //  Resume session (new clock-in on same day)
            user.setClock_in(LocalTime.now().withNano(0));
            user.setActive(true);
            return timeTrackerRepo.save(user);

        } else {
            //  New day or first-time entry
            if (user == null) user = new TimeTracker();

            user.setUser_id(id);
            user.setClock_in(LocalTime.now().withNano(0));
            user.setClockInDate(today);
            user.setActive(true);

            // Reset all fields for new day
            user.setBreakTime(0);
            user.setTotalMinutes(0);
            user.setWorkingMinutes(0);
            user.setTotalHours("0 hrs 0 mins");
            user.setWorkingHours("0 hrs 0 mins");
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
        }

        return timeTrackerRepo.save(user);
    }

    private String formatDuration(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return hours + " hrs " + minutes + " mins";
    }
}
