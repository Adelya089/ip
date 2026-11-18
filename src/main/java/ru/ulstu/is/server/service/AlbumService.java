package ru.ulstu.is.server.service;

import java.util.List;
import java.time.Year;
import ru.ulstu.is.server.error.InvalidValueException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.server.api.PageRs;
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
    public PageRs<AlbumRs> getPage(int page, int size) {
        var result = repo.findAll(
                PageRequest.of(page, size, Sort.by("id"))
        );
        return PageRs.fromPage(result, AlbumRs::fromEntity);
    }

    @Transactional(readOnly = true)
    public AlbumRs get(Long id) { return AlbumRs.fromEntity(getEntity(id)); }
    @Transactional
    public AlbumRs create(AlbumRq dto) {
        validateAlbum(dto);
        AlbumEntity e = new AlbumEntity();
        e.setTitle(dto.title().trim());
        e.setYear(dto.year());
        e.setTrackCount(0);
        e.setCover(normalizeCover(dto.cover()));
        e.setArtist(artistService.getEntity(dto.artistId()));
        return AlbumRs.fromEntity(repo.saveAndFlush(e));
    }
    @Transactional
    public AlbumRs update(Long id, AlbumRq dto) {
        validateAlbum(dto);
        AlbumEntity e = getEntity(id);
        e.setTitle(dto.title().trim());
        e.setYear(dto.year());
        e.setCover(normalizeCover(dto.cover()));
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
    private void validateAlbum(AlbumRq dto) {
        if (dto.title() == null || dto.title().trim().length() < 2 || dto.title().trim().length() > 100) {
            throw new InvalidValueException("Название должно содержать от 2 до 100 символов");
        }
        if (dto.year() == null || dto.year() < 1980 || dto.year() > Year.now().getValue()) {
            throw new InvalidValueException("Год должен быть от 1980 до текущего года");
        }
        normalizeCover(dto.cover());
    }

    private String normalizeCover(String cover) {
        if (cover == null || cover.isBlank()) return null;
        String value = cover.trim();
        if (value.length() > 255 || !(value.startsWith("https://") || value.startsWith("http://") || value.startsWith("/covers/"))) {
            throw new InvalidValueException("Укажите ссылку на изображение или /covers/имя-файла");
        }
        return value;
    }

    @Transactional(readOnly = true)
    public List<NamedCountProjection> stats(int year) { return repo.stats(year); }
}
