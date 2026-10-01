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

import ru.ulstu.is.server.api.album.AlbumRq;
import ru.ulstu.is.server.api.album.AlbumRs;
import ru.ulstu.is.server.api.artist.ArtistRq;
import ru.ulstu.is.server.api.artist.ArtistRs;
import ru.ulstu.is.server.api.genre.GenreRq;
import ru.ulstu.is.server.api.genre.GenreRs;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.service.AlbumService;
import ru.ulstu.is.server.service.ArtistService;
import ru.ulstu.is.server.service.GenreService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class AlbumServiceTests {
    @Autowired
    private AlbumService service;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private GenreService genreService;

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        final GenreRs genre = genreService.create(new GenreRq("Ню-метал"));
        final ArtistRs artist = artistService.create(new ArtistRq("Linkin Park", "США", genre.id()));

        service.create(new AlbumRq("Hybrid Theory", 2000, artist.id(), 12, ""));
        service.create(new AlbumRq("Meteora", 2003, artist.id(), 13, ""));
        final AlbumRs last = service.create(new AlbumRq("Minutes to Midnight", 2007, artist.id(), 12, ""));

        Assertions.assertEquals(3, service.getAll().size());

        final AlbumRs cmpEntity = service.get(3L);
        Assertions.assertEquals(last.id(), cmpEntity.id());
        Assertions.assertEquals(last.title(), cmpEntity.title());
        Assertions.assertEquals(last.year(), cmpEntity.year());
        Assertions.assertEquals(artist.id(), cmpEntity.artist().id());
    }

    @Test
    @Order(2)
    void updateTest() {
        final AlbumRs entity = service.get(3L);
        final String oldTitle = entity.title();
        final AlbumRs newEntity = service.update(3L,
                new AlbumRq("A Thousand Suns", 2010, entity.artist().id(), 15, ""));

        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals("A Thousand Suns", newEntity.title());
        Assertions.assertEquals(2010, newEntity.year());
        Assertions.assertNotEquals(oldTitle, newEntity.title());

        final AlbumRs cmpEntity = service.get(3L);
        Assertions.assertEquals(newEntity.id(), cmpEntity.id());
        Assertions.assertEquals(newEntity.title(), cmpEntity.title());
    }

    @Test
    @Order(3)
    void deleteTest() {
        service.delete(3L);
        Assertions.assertEquals(2, service.getAll().size());

        final AlbumRs last = service.get(2L);
        Assertions.assertEquals(2L, last.id());

        final AlbumRs newEntity = service.create(
                new AlbumRq("Living Things", 2012, last.artist().id(), 12, ""));
        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals(4L, newEntity.id());
    }
}