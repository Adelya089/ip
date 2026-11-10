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
@SpringBootTest @Transactional class TrackServiceUpdateTest {
    @Autowired TrackService service;
    @Autowired GenreService genreService;
    @Autowired ArtistService artistService;
    @Autowired AlbumService albumService;
    @Test void check() {
        var saved = service.create(new TrackRq("Первый трек", 200, albumService.create(new AlbumRq("Альбом", 2020, artistService.create(new ArtistRq("Исполнитель", "Россия", genreService.create(new GenreRq("Рок")).id())).id(), 2, null)).id()));
        var modified = service.update(saved.id(), new TrackRq("Второй трек", 240, albumService.create(new AlbumRq("Альбом2", 2021, artistService.create(new ArtistRq("Другой", "Россия", genreService.create(new GenreRq("Джаз")).id())).id(), 2, null)).id()));
        assertEquals(saved.id(), modified.id());
        assertNotEquals(saved.title(), modified.title());
    }
}
