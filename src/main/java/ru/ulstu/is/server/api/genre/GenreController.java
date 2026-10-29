
package ru.ulstu.is.server.api.genre;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.service.GenreService;

@RestController
@RequestMapping(Constants.API_URL + "/genres")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<GenreRs> getAll() {
        return service.getAll();
    }

    @GetMapping("/stats")
    public List<GenreStatsRs> stats(
            @RequestParam(defaultValue = "0") long minCount) {
        return service.stats(minCount).stream()
                .map(p -> new GenreStatsRs(p.getName(), p.getTotal()))
                .toList();
    }

    @GetMapping("/{id}")
    public GenreRs get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public GenreRs create(@RequestBody @Valid GenreRq dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public GenreRs update(@PathVariable Long id,
                          @RequestBody @Valid GenreRq dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public GenreRs delete(@PathVariable Long id) {
        return service.delete(id);
    }

    public record GenreStatsRs(String genreName, Long artistCount) {
    }
}
