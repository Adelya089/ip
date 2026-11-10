package ru.ulstu.is.server.album;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.server.api.genre.*;
import ru.ulstu.is.server.api.artist.*;
import ru.ulstu.is.server.api.album.*;
import ru.ulstu.is.server.api.track.*;
import ru.ulstu.is.server.service.*;
@SpringBootTest @Transactional class AlbumServiceCreateTest {
    @Autowired AlbumService service;
    @Autowired GenreService genreService;
    @Autowired ArtistService artistService;
    @Test void check() {
        var saved = service.create(new AlbumRq("Альбом", 2020, artistService.create(new ArtistRq("Исполнитель", "Россия", genreService.create(new GenreRq("Рок")).id())).id(), 2, null));
        assertNotNull(saved.id());
        assertEquals(saved.id(), service.get(saved.id()).id());
    }
}
