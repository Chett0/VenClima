package com.venclima.repository;

import com.venclima.dto.IslandNotificationDTO;
import com.venclima.model.Island;
import com.venclima.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IslandRepository extends JpaRepository<Island, Integer> {
    Optional<Island> findByName(String name);
    @Query(value = """
                    SELECT
                        i.name AS islandName,
                        i.id   AS islandId,
                        CASE WHEN n.user_id IS NULL Then FALSE Else TRUE END  as isNotified
                    FROM islands i
                    LEFT JOIN notifications n
                        ON i.id = n.island_id
                       AND n.user_id = :userId
                """,
            nativeQuery = true)
    List<IslandNotificationDTO> getNotification(
            @Param("userId") Integer userId
    );
}
