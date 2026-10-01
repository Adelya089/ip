package ru.ulstu.is.server.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.server.configuration.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Constants.API_URL + GenreController.URL)
public class GenreController {
    public static final String URL = "/genres";

    private final Logger log = LoggerFactory.getLogger(GenreController.class);
    private final List<GenreDto> genres = new ArrayList<>();

    public GenreController() {
        genres.add(new GenreDto("1", "Ню-метал"));
        genres.add(new GenreDto("2", "Альтернативный рок"));
        genres.add(new GenreDto("3", "Пост-панк"));
        genres.add(new GenreDto("4", "Хэви-метал"));
        genres.add(new GenreDto("5", "Электроника"));
    }

    @GetMapping
    public List<GenreDto> getAll() {
        log.debug("getAll genres");
        return genres;
    }

    @GetMapping("/{id}")
    public GenreDto getById(@PathVariable String id) {
        log.debug("getById genre id = {}", id);
        return genres.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Genre", id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto create(@RequestBody GenreDto genre) {
        log.debug("create genre = {}", genre.getName());
        genre.setId(UUID.randomUUID().toString());
        genres.add(genre);
        return genre;
    }

    @PutMapping("/{id}")
    public GenreDto update(@PathVariable String id, @RequestBody GenreDto genre) {
        log.debug("update genre id = {}", id);
        GenreDto existing = genres.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Genre", id));
        existing.setName(genre.getName());
        return existing;
    }

    @DeleteMapping("/{id}")
    public GenreDto delete(@PathVariable String id) {
        log.debug("delete genre id = {}", id);
        GenreDto existing = genres.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Genre", id));
        genres.remove(existing);
        return existing;
    }
}