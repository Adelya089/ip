package ru.ulstu.is.server.api.artist;

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
import ru.ulstu.is.server.service.ArtistService;

@RestController
@RequestMapping(Constants.API_URL + ArtistController.URL)
public class ArtistController {
    public static final String URL = "/artists";
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public List<ArtistRs> getAll() {
        return artistService.getAll();
    }

    @GetMapping("/{id}")
    public ArtistRs get(@PathVariable Long id) {
        return artistService.get(id);
    }

    @PostMapping
    public ArtistRs create(@RequestBody @Valid ArtistRq dto) {
        return artistService.create(dto);
    }

    @PutMapping("/{id}")
    public ArtistRs update(@PathVariable Long id, @RequestBody @Valid ArtistRq dto) {
        return artistService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ArtistRs delete(@PathVariable Long id) {
        return artistService.delete(id);
    }
}