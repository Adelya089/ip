package ru.ulstu.is.server.entity;

import jakarta.persistence.*;
import java.util.Objects;
@MappedSuperclass public abstract class BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    @Override public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseEntity other = (BaseEntity) obj;
        return id != null && Objects.equals(id, other.id);
    }
    @Override public final int hashCode() { return getClass().hashCode(); }
}
