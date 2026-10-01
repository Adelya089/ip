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
@RequestMapping(Constants.API_URL + ArtistController.URL)
public class ArtistController {
    public static final String URL = "/artists";

    private final Logger log = LoggerFactory.getLogger(ArtistController.class);
    private final List<ArtistDto> artists = new ArrayList<>();

    public ArtistController() {
        artists.add(new ArtistDto("1", "Linkin Park", "США", "1"));
        artists.add(new ArtistDto("2", "Coldplay", "Великобритания", "2"));
        artists.add(new ArtistDto("3", "Кино", "СССР", "3"));
        artists.add(new ArtistDto("4", "Aria", "Россия", "4"));
        artists.add(new ArtistDto("5", "Daft Punk", "Франция", "5"));
    }

    @GetMapping
    public List<ArtistDto> getAll() {
        log.debug("getAll artists");
        return artists;
    }

    @GetMapping("/{id}")
    public ArtistDto getById(@PathVariable String id) {
        log.debug("getById artist id = {}", id);
        return artists.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Artist", id));
    }
    @GetMapping("/country")
    public List<ArtistDto> getWithShortCountry() {
        log.debug("getCountry");
        return artists.stream()
                .filter(a -> a.getCountry() != null && a.getCountry().length() <= 4)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistDto create(@RequestBody ArtistDto artist) {
        log.debug("create artist = {}", artist.getName());
        artist.setId(UUID.randomUUID().toString());
        artists.add(artist);
        return artist;
    }

    @PutMapping("/{id}")
    public ArtistDto update(@PathVariable String id, @RequestBody ArtistDto artist) {
        log.debug("update artist id = {}", id);
        ArtistDto existing = artists.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Artist", id));
        existing.setName(artist.getName());
        existing.setCountry(artist.getCountry());
        existing.setGenreId(artist.getGenreId());
        return existing;
    }

    @DeleteMapping("/{id}")
    public ArtistDto delete(@PathVariable String id) {
        log.debug("delete artist id = {}", id);
        ArtistDto existing = artists.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Artist", id));
        artists.remove(existing);
        return existing;
    }
}