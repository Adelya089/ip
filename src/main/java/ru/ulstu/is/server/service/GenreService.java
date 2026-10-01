package ru.ulstu.is.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.ulstu.is.server.api.genre.GenreRq;
import ru.ulstu.is.server.api.genre.GenreRs;
import ru.ulstu.is.server.entity.GenreEntity;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.repository.GenreRepository;

@Service
public class GenreService {
    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public GenreEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre", id));
    }

    public List<GenreRs> getAll() {
        return repository.findAll().stream().map(GenreRs::fromEntity).toList();
    }

    public GenreRs get(Long id) {
        final GenreEntity entity = getEntity(id);
        return GenreRs.fromEntity(entity);
    }

    public GenreRs create(GenreRq dto) {
        final GenreEntity entity = new GenreEntity();
        entity.setName(dto.name());
        return GenreRs.fromEntity(repository.save(entity));
    }

    public GenreRs update(Long id, GenreRq dto) {
        final GenreEntity entity = getEntity(id);
        entity.setName(dto.name());
        return GenreRs.fromEntity(repository.save(entity));
    }

    public GenreRs delete(Long id) {
        final GenreEntity entity = getEntity(id);
        repository.delete(entity);
        return GenreRs.fromEntity(entity);
    }
}