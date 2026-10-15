package ru.ulstu.is.server.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.server.api.genre.*;
import ru.ulstu.is.server.entity.*;
import ru.ulstu.is.server.repository.GenreRepository;
import ru.ulstu.is.server.error.NotFoundException;
@Service public class GenreService {
    private final GenreRepository repo;
    public GenreService(GenreRepository repo) { this.repo=repo; }
    @Transactional(readOnly = true)
    public GenreEntity getEntity(Long id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Genre", id)); }
    @Transactional(readOnly = true)
    public List<GenreRs> getAll() { return repo.findAll().stream().map(GenreRs::fromEntity).toList(); }
    @Transactional(readOnly = true)
    public GenreRs get(Long id) { return GenreRs.fromEntity(getEntity(id)); }
    @Transactional
    public GenreRs create(GenreRq dto) {
        GenreEntity e = new GenreEntity();
        e.setName(dto.name());
        return GenreRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public GenreRs update(Long id, GenreRq dto) {
        GenreEntity e = getEntity(id);
        e.setName(dto.name());
        return GenreRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public GenreRs delete(Long id) {
        GenreEntity e = getEntity(id);
        if (!e.getArtists().isEmpty())
            throw new IllegalArgumentException("Нельзя удалить объект со связанными записями");
        GenreRs result = GenreRs.fromEntity(e);
        repo.delete(e);
        repo.flush();
        return result;
    }
    @Transactional(readOnly = true)
    public List<NamedCountProjection> stats(long minCount) { return repo.stats(minCount); }
}
