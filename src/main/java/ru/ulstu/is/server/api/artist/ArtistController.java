package ru.ulstu.is.server.api.artist;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.entity.NamedCountProjection;
import ru.ulstu.is.server.service.ArtistService;
@RestController @RequestMapping(Constants.API_URL + "/artists")
public class ArtistController {
    private final ArtistService service;
    public ArtistController(ArtistService service) { this.service = service; }
    @GetMapping public List<ArtistRs> getAll() { return service.getAll(); }
    @GetMapping("/stats") public List<NamedCountProjection> stats(@RequestParam(defaultValue="") String filter) { return service.stats(filter); }
    @GetMapping("/{id}") public ArtistRs get(@PathVariable Long id) { return service.get(id); }
    @PostMapping public ArtistRs create(@RequestBody @Valid ArtistRq dto) { return service.create(dto); }
    @PutMapping("/{id}") public ArtistRs update(@PathVariable Long id, @RequestBody @Valid ArtistRq dto) { return service.update(id,dto); }
    @DeleteMapping("/{id}") public ArtistRs delete(@PathVariable Long id) { return service.delete(id); }
}
