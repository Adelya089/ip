package ru.ulstu.is.server.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.server.api.artist.*;
import ru.ulstu.is.server.entity.*;
import ru.ulstu.is.server.repository.ArtistRepository;
import ru.ulstu.is.server.error.NotFoundException;
@Service public class ArtistService {
    private final ArtistRepository repo;
    private final GenreService genreService;
    public ArtistService(ArtistRepository repo, GenreService genreService) { this.repo=repo; this.genreService=genreService; }
    @Transactional(readOnly = true)
    public ArtistEntity getEntity(Long id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Artist", id)); }
    @Transactional(readOnly = true)
    public List<ArtistRs> getAll() { return repo.findAll().stream().map(ArtistRs::fromEntity).toList(); }
    @Transactional(readOnly = true)
    public ArtistRs get(Long id) { return ArtistRs.fromEntity(getEntity(id)); }
    @Transactional
    public ArtistRs create(ArtistRq dto) {
        ArtistEntity e = new ArtistEntity();
        e.setName(dto.name());
        e.setCountry(dto.country());
        e.setGenre(genreService.getEntity(dto.genreId()));
        return ArtistRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public ArtistRs update(Long id, ArtistRq dto) {
        ArtistEntity e = getEntity(id);
        e.setName(dto.name());
        e.setCountry(dto.country());
        e.setGenre(genreService.getEntity(dto.genreId()));
        return ArtistRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public ArtistRs delete(Long id) {
        ArtistEntity e = getEntity(id);
        if (!e.getAlbums().isEmpty())
            throw new IllegalArgumentException("Нельзя удалить объект со связанными записями");
        ArtistRs result = ArtistRs.fromEntity(e);
        repo.delete(e);
        repo.flush();
        return result;
    }
    @Transactional(readOnly = true)
    public List<NamedCountProjection> stats(String filter) { return repo.stats(filter); }
}
