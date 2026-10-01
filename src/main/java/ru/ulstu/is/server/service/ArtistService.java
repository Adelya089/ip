package ru.ulstu.is.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.ulstu.is.server.api.artist.ArtistRq;
import ru.ulstu.is.server.api.artist.ArtistRs;
import ru.ulstu.is.server.entity.ArtistEntity;
import ru.ulstu.is.server.entity.GenreEntity;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.repository.ArtistRepository;

@Service
public class ArtistService {
    private final ArtistRepository repository;
    private final GenreService genreService;

    public ArtistService(ArtistRepository repository, GenreService genreService) {
        this.repository = repository;
        this.genreService = genreService;
    }

    public ArtistEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artist", id));
    }

    public List<ArtistRs> getAll() {
        return repository.findAll().stream().map(ArtistRs::fromEntity).toList();
    }

    public ArtistRs get(Long id) {
        final ArtistEntity entity = getEntity(id);
        return ArtistRs.fromEntity(entity);
    }

    public ArtistRs create(ArtistRq dto) {
        final GenreEntity genre = genreService.getEntity(dto.genreId());
        final ArtistEntity entity = new ArtistEntity();
        entity.setName(dto.name());
        entity.setCountry(dto.country());
        entity.setGenre(genre);
        return ArtistRs.fromEntity(repository.save(entity));
    }

    public ArtistRs update(Long id, ArtistRq dto) {
        final GenreEntity genre = genreService.getEntity(dto.genreId());
        final ArtistEntity entity = getEntity(id);
        entity.setName(dto.name());
        entity.setCountry(dto.country());
        entity.setGenre(genre);
        return ArtistRs.fromEntity(repository.save(entity));
    }

    public ArtistRs delete(Long id) {
        final ArtistEntity entity = getEntity(id);
        repository.delete(entity);
        return ArtistRs.fromEntity(entity);
    }
}