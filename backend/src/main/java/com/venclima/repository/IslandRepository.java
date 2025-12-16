package com.venclima.repository;

import com.venclima.model.Island;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IslandRepository extends JpaRepository<Island, Integer> {
    Optional<Island> findByName(String name);
}
