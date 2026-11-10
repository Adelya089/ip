
package ru.ulstu.is.server.api.album;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ru.ulstu.is.server.api.PageRs;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.service.AlbumService;

@RestController
@RequestMapping(Constants.API_URL + "/albums")
public class AlbumController {
    private final AlbumService service;

    public AlbumController(AlbumService service) {
        this.service = service;
    }
    @GetMapping("/all")
    public List<AlbumRs> getAllWithoutPagination() {
        return service.getAll();
    }

    @GetMapping
    public PageRs<AlbumRs> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size) {
        return service.getPage(
                Math.max(page, 1) - 1,
                Math.max(size, 1)
        );
    }


    @GetMapping("/stats")
    public List<AlbumStatsRs> stats(
            @RequestParam(defaultValue = "1980") int year) {
        return service.stats(year).stream()
                .map(p -> new AlbumStatsRs(p.getName(), p.getTotal()))
                .toList();
    }

    @GetMapping("/{id}")
    public AlbumRs get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public AlbumRs create(@RequestBody @Valid AlbumRq dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public AlbumRs update(@PathVariable Long id,
                          @RequestBody @Valid AlbumRq dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public AlbumRs delete(@PathVariable Long id) {
        return service.delete(id);
    }

    public record AlbumStatsRs(String albumTitle, Long trackCount) {
    }
}
