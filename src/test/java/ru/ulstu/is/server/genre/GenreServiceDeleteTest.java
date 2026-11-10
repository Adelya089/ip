package ru.ulstu.is.server.genre;
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
@SpringBootTest @Transactional class GenreServiceDeleteTest {
    @Autowired GenreService service;
    @Autowired ArtistService artistService;
    @Autowired AlbumService albumService;
    @Test void check() {
        var saved = service.create(new GenreRq("Рок"));
        service.delete(saved.id());
        assertThrows(ru.ulstu.is.server.error.NotFoundException.class, () -> service.get(saved.id()));
    }
}
