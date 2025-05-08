package com.timeTracking.Time_Tracking_Service.service;

import com.timeTracking.Time_Tracking_Service.model.TimeTracker;
import com.timeTracking.Time_Tracking_Service.repo.TimeTrackerRepo;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TimeTrackerService {

    private TimeTrackerRepo timeTrackerRepo;
    public TimeTrackerService(TimeTrackerRepo timeTrackerRepo) {
        this.timeTrackerRepo = timeTrackerRepo;
    }

    public TimeTracker toggle(long id) {

        TimeTracker User = timeTrackerRepo.findById(id).get();

        Duration duration;
        String totalWorkingHours ;
        long hours=0;
        long minutes=0;

        if (User.isActive()) {
            // Clock out
            User.setClock_Out(LocalTime.now().withNano(0));
            User.setClockOutDate(LocalDate.now());

            if(User.getClockOutDate().equals(User.getClockInDate())){
                     duration = Duration.between(User.getClock_in(), User.getClock_Out());
                    hours +=duration.toHours();
                    minutes +=duration.toMinutes();
                    totalWorkingHours=hours+" hrs "+minutes+" mins";

                    User.setTotalWorkingHours(totalWorkingHours);
            }
            User.setActive(false);
            return timeTrackerRepo.save(User);
        } else {
            // Clock in
            TimeTracker newRecord = new TimeTracker();
            newRecord.setClock_in(LocalTime.now().withNano(0));
            newRecord.setActive(true);
            newRecord.setClockInDate(LocalDate.now());
            return timeTrackerRepo.save(newRecord);
        }
    }
}
