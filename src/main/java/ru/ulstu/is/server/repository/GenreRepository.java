package ru.ulstu.is.server.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.ulstu.is.server.entity.GenreEntity;
import java.util.List;
public interface GenreRepository extends JpaRepository<GenreEntity, Long> { 
    @Query("SELECT g.name AS name, COUNT(a) AS total FROM GenreEntity g LEFT JOIN g.artists a GROUP BY g.id, g.name HAVING COUNT(a) >= :minCount ORDER BY g.name")
    List<ru.ulstu.is.server.entity.NamedCountProjection> stats(@Param("minCount") long minCount);
 }
