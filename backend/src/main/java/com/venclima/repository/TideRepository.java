package com.venclima.repository;

import com.venclima.model.Tide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TideRepository extends JpaRepository<Tide, Integer> {
    //List<Tide> findByStation_Id(int stationId);
    Optional<Tide> findByStation_IdAndDate(Integer stationId, LocalDateTime date);
}
