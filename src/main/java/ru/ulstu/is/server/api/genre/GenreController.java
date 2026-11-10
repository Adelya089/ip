
package ru.ulstu.is.server.api.genre;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ru.ulstu.is.server.api.PageRs;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.service.GenreService;

@RestController
@RequestMapping(Constants.API_URL + "/genres")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }
    @GetMapping("/all")
    public List<GenreRs> getAllWithoutPagination() {
        return service.getAll();
    }

    @GetMapping
    public PageRs<GenreRs> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size) {
        return service.getPage(
                Math.max(page, 1) - 1,
                Math.max(size, 1)
        );
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
