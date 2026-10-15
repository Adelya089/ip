package ru.ulstu.is.server.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.server.api.album.*;
import ru.ulstu.is.server.entity.*;
import ru.ulstu.is.server.repository.AlbumRepository;
import ru.ulstu.is.server.error.NotFoundException;
@Service public class AlbumService {
    private final AlbumRepository repo;
    private final ArtistService artistService;
    public AlbumService(AlbumRepository repo, ArtistService artistService) { this.repo=repo; this.artistService=artistService; }
    @Transactional(readOnly = true)
    public AlbumEntity getEntity(Long id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Album", id)); }
    @Transactional(readOnly = true)
    public List<AlbumRs> getAll() { return repo.findAll().stream().map(AlbumRs::fromEntity).toList(); }
    @Transactional(readOnly = true)
    public AlbumRs get(Long id) { return AlbumRs.fromEntity(getEntity(id)); }
    @Transactional
    public AlbumRs create(AlbumRq dto) {
        AlbumEntity e = new AlbumEntity();
        e.setTitle(dto.title());
        e.setYear(dto.year());
        e.setTrackCount(dto.trackCount());
        e.setCover(dto.cover());
        e.setArtist(artistService.getEntity(dto.artistId()));
        return AlbumRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public AlbumRs update(Long id, AlbumRq dto) {
        AlbumEntity e = getEntity(id);
        e.setTitle(dto.title());
        e.setYear(dto.year());
        e.setTrackCount(dto.trackCount());
        e.setCover(dto.cover());
        e.setArtist(artistService.getEntity(dto.artistId()));
        return AlbumRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public AlbumRs delete(Long id) {
        AlbumEntity e = getEntity(id);
        if (!e.getTracks().isEmpty())
            throw new IllegalArgumentException("Нельзя удалить объект со связанными записями");
        AlbumRs result = AlbumRs.fromEntity(e);
        repo.delete(e);
        repo.flush();
        return result;
    }
    @Transactional(readOnly = true)
    public List<NamedCountProjection> stats(int year) { return repo.stats(year); }
}
