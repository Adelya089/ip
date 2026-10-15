package ru.ulstu.is.server.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.ulstu.is.server.entity.AlbumEntity;
import java.util.List;
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> { 
    @Query("SELECT a.title AS name, COUNT(t) AS total FROM AlbumEntity a LEFT JOIN a.tracks t WHERE a.year >= :year GROUP BY a.id, a.title ORDER BY a.title")
    List<ru.ulstu.is.server.entity.NamedCountProjection> stats(@Param("year") int year);
 }
