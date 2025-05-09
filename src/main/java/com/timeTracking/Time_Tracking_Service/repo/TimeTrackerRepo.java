package com.timeTracking.Time_Tracking_Service.repo;

import com.timeTracking.Time_Tracking_Service.model.TimeTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTrackerRepo extends JpaRepository<TimeTracker, Long> {
}