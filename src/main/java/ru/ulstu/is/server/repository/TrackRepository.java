package ru.ulstu.is.server.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.ulstu.is.server.entity.TrackEntity;
import java.util.List;
public interface TrackRepository extends JpaRepository<TrackEntity, Long> { 
    @Query("SELECT t.album.title AS name, COUNT(t) AS total, AVG(t.duration) AS averageDuration FROM TrackEntity t WHERE t.duration >= :minDuration GROUP BY t.album.id, t.album.title ORDER BY t.album.title")
    List<ru.ulstu.is.server.entity.TrackStatsProjection> stats(@Param("minDuration") int minDuration);
 }
