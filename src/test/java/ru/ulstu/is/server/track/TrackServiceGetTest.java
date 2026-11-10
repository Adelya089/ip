package ru.ulstu.is.server.track;
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
@SpringBootTest @Transactional class TrackServiceGetTest {
    @Autowired TrackService service;
    @Autowired GenreService genreService;
    @Autowired ArtistService artistService;
    @Autowired AlbumService albumService;
    @Test void check() {
        var saved = service.create(new TrackRq("Первый трек", 200, albumService.create(new AlbumRq("Альбом", 2020, artistService.create(new ArtistRq("Исполнитель", "Россия", genreService.create(new GenreRq("Рок")).id())).id(), 2, null)).id()));
        assertEquals(saved.id(), service.get(saved.id()).id());
    }
}
