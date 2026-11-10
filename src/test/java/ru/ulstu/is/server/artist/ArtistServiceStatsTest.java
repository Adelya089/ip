package ru.ulstu.is.server.artist;
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
@SpringBootTest @Transactional class ArtistServiceStatsTest {
    @Autowired ArtistService service;
    @Autowired GenreService genreService;
    @Autowired AlbumService albumService;
    @Test void check() {
        service.create(new ArtistRq("Исполнитель", "Россия", genreService.create(new GenreRq("Рок")).id()));
        assertFalse(service.stats("").isEmpty());
    }
}
