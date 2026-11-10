
package ru.ulstu.is.server.api.track;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ru.ulstu.is.server.api.PageRs;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.service.TrackService;

@RestController
@RequestMapping(Constants.API_URL + "/tracks")
public class TrackController {
    private final TrackService service;

    public TrackController(TrackService service) {
        this.service = service;
    }


    @GetMapping
    public PageRs<TrackRs> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size) {
        return service.getPage(
                Math.max(page, 1) - 1,
                Math.max(size, 1)
        );
    }
    @GetMapping("/all")
    public List<TrackRs> getAllWithoutPagination() {
        return service.getAll();
    }

    @GetMapping("/stats")
    public List<TrackStatsRs> stats(
            @RequestParam(defaultValue = "0") int minDuration) {
        return service.stats(minDuration).stream()
                .map(p -> new TrackStatsRs(
                        p.getName(),
                        p.getTotal(),
                        p.getAverageDuration()))
                .toList();
    }

    @GetMapping("/{id}")
    public TrackRs get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public TrackRs create(@RequestBody @Valid TrackRq dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TrackRs update(@PathVariable Long id,
                          @RequestBody @Valid TrackRq dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public TrackRs delete(@PathVariable Long id) {
        return service.delete(id);
    }

    public record TrackStatsRs(
            String albumTitle,
            Long trackCount,
            Double averageDurationSeconds) {
    }
}
