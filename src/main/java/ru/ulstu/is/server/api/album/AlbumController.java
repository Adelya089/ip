package ru.ulstu.is.server.api.album;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.service.AlbumService;

@RestController
@RequestMapping(Constants.API_URL + AlbumController.URL)
public class AlbumController {
    public static final String URL = "/albums";
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public List<AlbumRs> getAll() {
        return albumService.getAll();
    }

    @GetMapping("/{id}")
    public AlbumRs get(@PathVariable Long id) {
        return albumService.get(id);
    }

    @PostMapping
    public AlbumRs create(@RequestBody @Valid AlbumRq dto) {
        return albumService.create(dto);
    }

    @PutMapping("/{id}")
    public AlbumRs update(@PathVariable Long id, @RequestBody @Valid AlbumRq dto) {
        return albumService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public AlbumRs delete(@PathVariable Long id) {
        return albumService.delete(id);
    }
}