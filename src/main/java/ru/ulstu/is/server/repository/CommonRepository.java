package ru.ulstu.is.server.repository;

import java.util.List;
import java.util.Optional;

public interface CommonRepository<E, T> {
    List<E> findAll();

    Optional<E> findById(T id);

    E save(E entity);

    void delete(E entity);

    void deleteAll();
}