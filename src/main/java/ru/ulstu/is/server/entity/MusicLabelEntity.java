
package ru.ulstu.is.server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "music_label")
public class MusicLabelEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "music_label_generator"
    )
    @SequenceGenerator(
            name = "music_label_generator",
            sequenceName = "music_label_seq",
            allocationSize = 50
    )
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
