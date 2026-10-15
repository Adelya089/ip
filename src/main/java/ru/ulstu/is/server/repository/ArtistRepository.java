package ru.ulstu.is.server.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.ulstu.is.server.entity.ArtistEntity;
import java.util.List;
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> { 
    @Query("SELECT a.name AS name, COUNT(al) AS total FROM ArtistEntity a LEFT JOIN a.albums al WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :filter, '%')) GROUP BY a.id, a.name ORDER BY a.name")
    List<ru.ulstu.is.server.entity.NamedCountProjection> stats(@Param("filter") String filter);
 }
