package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ru.ulstu.is.server.api.artist.ArtistRq;
import ru.ulstu.is.server.api.artist.ArtistRs;
import ru.ulstu.is.server.api.genre.GenreRq;
import ru.ulstu.is.server.api.genre.GenreRs;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.service.ArtistService;
import ru.ulstu.is.server.service.GenreService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class ArtistServiceTests {
    @Autowired
    private ArtistService service;

    @Autowired
    private GenreService genreService;

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        final GenreRs genre = genreService.create(new GenreRq("Рок"));
        service.create(new ArtistRq("Linkin Park", "США", genre.id()));
        service.create(new ArtistRq("Coldplay", "Великобритания", genre.id()));
        final ArtistRs last = service.create(new ArtistRq("Кино", "СССР", genre.id()));

        Assertions.assertEquals(3, service.getAll().size());

        final ArtistRs cmpEntity = service.get(3L);
        Assertions.assertEquals(last.id(), cmpEntity.id());
        Assertions.assertEquals(last.name(), cmpEntity.name());
        Assertions.assertEquals(last.country(), cmpEntity.country());
        Assertions.assertEquals(genre.id(), cmpEntity.genre().id());
    }

    @Test
    @Order(2)
    void updateTest() {
        final ArtistRs entity = service.get(3L);
        final String oldCountry = entity.country();
        final ArtistRs newEntity = service.update(3L,
                new ArtistRq("Кино", "Россия", entity.genre().id()));

        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals("Россия", newEntity.country());
        Assertions.assertNotEquals(oldCountry, newEntity.country());

        final ArtistRs cmpEntity = service.get(3L);
        Assertions.assertEquals(newEntity.id(), cmpEntity.id());
        Assertions.assertEquals(newEntity.country(), cmpEntity.country());
    }

    @Test
    @Order(3)
    void deleteTest() {
        service.delete(3L);
        Assertions.assertEquals(2, service.getAll().size());

        final ArtistRs last = service.get(2L);
        Assertions.assertEquals(2L, last.id());

        final GenreRs genre = genreService.getAll().get(0);
        final ArtistRs newEntity = service.create(new ArtistRq("Aria", "Россия", genre.id()));
        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals(4L, newEntity.id());
    }
}