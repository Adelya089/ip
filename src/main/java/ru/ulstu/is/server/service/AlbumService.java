package ru.ulstu.is.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.ulstu.is.server.api.album.AlbumRq;
import ru.ulstu.is.server.api.album.AlbumRs;
import ru.ulstu.is.server.entity.AlbumEntity;
import ru.ulstu.is.server.entity.ArtistEntity;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.repository.AlbumRepository;

@Service
public class AlbumService {
    private final AlbumRepository repository;
    private final ArtistService artistService;

    public AlbumService(AlbumRepository repository, ArtistService artistService) {
        this.repository = repository;
        this.artistService = artistService;
    }

    public AlbumEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Album", id));
    }

    public List<AlbumRs> getAll() {
        return repository.findAll().stream().map(AlbumRs::fromEntity).toList();
    }

    public AlbumRs get(Long id) {
        final AlbumEntity entity = getEntity(id);
        return AlbumRs.fromEntity(entity);
    }

    public AlbumRs create(AlbumRq dto) {
        final ArtistEntity artist = artistService.getEntity(dto.artistId());
        final AlbumEntity entity = new AlbumEntity();
        entity.setTitle(dto.title());
        entity.setYear(dto.year());
        entity.setArtist(artist);
        entity.setTrackCount(dto.trackCount());
        entity.setCover(dto.cover());
        return AlbumRs.fromEntity(repository.save(entity));
    }

    public AlbumRs update(Long id, AlbumRq dto) {
        final ArtistEntity artist = artistService.getEntity(dto.artistId());
        final AlbumEntity entity = getEntity(id);
        entity.setTitle(dto.title());
        entity.setYear(dto.year());
        entity.setArtist(artist);
        entity.setTrackCount(dto.trackCount());
        entity.setCover(dto.cover());
        return AlbumRs.fromEntity(repository.save(entity));
    }

    public AlbumRs delete(Long id) {
        final AlbumEntity entity = getEntity(id);
        repository.delete(entity);
        return AlbumRs.fromEntity(entity);
    }
}