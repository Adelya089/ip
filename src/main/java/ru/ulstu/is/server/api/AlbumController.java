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
@RequestMapping(Constants.API_URL + AlbumController.URL)
public class AlbumController {
    public static final String URL = "/albums";

    private final Logger log = LoggerFactory.getLogger(AlbumController.class);
    private final List<AlbumDto> albums = new ArrayList<>();

    public AlbumController() {
        albums.add(new AlbumDto("1", "Hybrid Theory", 2000, "1", 12, ""));
        albums.add(new AlbumDto("2", "Meteora", 2003, "1", 13, ""));
        albums.add(new AlbumDto("3", "Parachutes", 2000, "2", 10, ""));
        albums.add(new AlbumDto("4", "Группа крови", 1988, "3", 11, ""));
        albums.add(new AlbumDto("5", "Кровь за кровь", 1991, "4", 10, ""));
        albums.add(new AlbumDto("6", "Random Access Memories", 2013, "5", 13, ""));
    }

    @GetMapping
    public List<AlbumDto> getAll() {
        log.debug("getAll albums");
        return albums;
    }

    @GetMapping("/{id}")
    public AlbumDto getById(@PathVariable String id) {
        log.debug("getById album id = {}", id);
        return albums.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Album", id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumDto create(@RequestBody AlbumDto album) {
        log.debug("create album = {}", album.getTitle());
        album.setId(UUID.randomUUID().toString());
        albums.add(album);
        return album;
    }

    @PutMapping("/{id}")
    public AlbumDto update(@PathVariable String id, @RequestBody AlbumDto album) {
        log.debug("update album id = {}", id);
        AlbumDto existing = albums.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Album", id));
        existing.setTitle(album.getTitle());
        existing.setYear(album.getYear());
        existing.setArtistId(album.getArtistId());
        existing.setTrackCount(album.getTrackCount());
        existing.setCover(album.getCover());
        return existing;
    }

    @DeleteMapping("/{id}")
    public AlbumDto delete(@PathVariable String id) {
        log.debug("delete album id = {}", id);
        AlbumDto existing = albums.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Album", id));
        albums.remove(existing);
        return existing;
    }
}