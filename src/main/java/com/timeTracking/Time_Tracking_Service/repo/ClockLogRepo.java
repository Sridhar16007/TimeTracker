package com.timeTracking.Time_Tracking_Service.repo;

import com.timeTracking.Time_Tracking_Service.model.ClockLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClockLogRepo extends JpaRepository<ClockLog, Long> {
    List<ClockLog> findByUserIdAndEventDateOrderByEventTime(long userId, LocalDate date);
}