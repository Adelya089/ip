package ru.ulstu.is.server.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.server.api.PageRs;
import ru.ulstu.is.server.api.track.*;
import ru.ulstu.is.server.entity.*;
import ru.ulstu.is.server.repository.TrackRepository;
import ru.ulstu.is.server.error.NotFoundException;
@Service public class TrackService {
    private final TrackRepository repo;
    private final AlbumService albumService;
    public TrackService(TrackRepository repo, AlbumService albumService) { this.repo=repo; this.albumService=albumService; }
    @Transactional(readOnly = true)
    public TrackEntity getEntity(Long id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Track", id)); }
    @Transactional(readOnly = true)
    public List<TrackRs> getAll() { return repo.findAll().stream().map(TrackRs::fromEntity).toList(); }

    @Transactional(readOnly = true)
    public PageRs<TrackRs> getPage(int page, int size) {
        var result = repo.findAll(
                PageRequest.of(page, size, Sort.by("id"))
        );
        return PageRs.fromPage(result, TrackRs::fromEntity);
    }

    @Transactional(readOnly = true)
    public TrackRs get(Long id) { return TrackRs.fromEntity(getEntity(id)); }
    @Transactional
    public TrackRs create(TrackRq dto) {
        TrackEntity e = new TrackEntity();
        e.setTitle(dto.title());
        e.setDuration(dto.duration());
        e.setAlbum(albumService.getEntity(dto.albumId()));
        return TrackRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public TrackRs update(Long id, TrackRq dto) {
        TrackEntity e = getEntity(id);
        e.setTitle(dto.title());
        e.setDuration(dto.duration());
        e.setAlbum(albumService.getEntity(dto.albumId()));
        return TrackRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public TrackRs delete(Long id) {
        TrackEntity e = getEntity(id);
        TrackRs result = TrackRs.fromEntity(e);
        repo.delete(e);
        repo.flush();
        return result;
    }
    @Transactional(readOnly = true)
    public List<TrackStatsProjection> stats(int minDuration) { return repo.stats(minDuration); }
}
