package com.venclima.repository;

import com.venclima.model.TideInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TideInfoRepository extends JpaRepository<TideInfo, Long> {
}
