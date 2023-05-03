package com.example.backend.repository;

import com.example.backend.domain.SpeedData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpeedDataRepository extends JpaRepository<SpeedData, Long> {

    @Query(value = "SELECT count(*) FROM (SELECT 1 FROM data_table LIMIT 1) AS t", nativeQuery = true)
    int checkIfEmpty();

    @Query("select d from SpeedData d " +
            "where ((cast(:speed as int) is null) or d.speed >= :speed) " +
            "and ((cast(:from as timestamp) is null) or d.time >= :from) " +
            "and ((cast(:to as timestamp) is null) or d.time <= :to)")
    List<SpeedData> filterData(@Param("from") LocalDateTime from,
                               @Param("to") LocalDateTime to,
                               @Param("speed") Integer speed, Pageable pageable);

    @Query("select d from SpeedData d where d.time >= :from and d.time < :to")
    List<SpeedData> getSpecificDayData(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
