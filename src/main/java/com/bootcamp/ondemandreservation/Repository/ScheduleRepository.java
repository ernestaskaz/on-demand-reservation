package com.bootcamp.ondemandreservation.Repository;

import com.bootcamp.ondemandreservation.Model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
