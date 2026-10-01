package ru.ulstu.is.server.api.genre;

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
import ru.ulstu.is.server.service.GenreService;

@RestController
@RequestMapping(Constants.API_URL + GenreController.URL)
public class GenreController {
    public static final String URL = "/genres";
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<GenreRs> getAll() {
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public GenreRs get(@PathVariable Long id) {
        return genreService.get(id);
    }

    @PostMapping
    public GenreRs create(@RequestBody @Valid GenreRq dto) {
        return genreService.create(dto);
    }

    @PutMapping("/{id}")
    public GenreRs update(@PathVariable Long id, @RequestBody @Valid GenreRq dto) {
        return genreService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public GenreRs delete(@PathVariable Long id) {
        return genreService.delete(id);
    }
}