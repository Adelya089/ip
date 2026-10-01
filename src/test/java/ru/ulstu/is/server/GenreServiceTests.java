package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ru.ulstu.is.server.api.genre.GenreRq;
import ru.ulstu.is.server.api.genre.GenreRs;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.service.GenreService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class GenreServiceTests {
    @Autowired
    private GenreService service;

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        service.create(new GenreRq("Ню-метал"));
        service.create(new GenreRq("Альтернативный рок"));
        final GenreRs last = service.create(new GenreRq("Пост-панк"));

        Assertions.assertEquals(3, service.getAll().size());

        final GenreRs cmpEntity = service.get(3L);
        Assertions.assertEquals(last.id(), cmpEntity.id());
        Assertions.assertEquals(last.name(), cmpEntity.name());
    }

    @Test
    @Order(2)
    void updateTest() {
        final String test = "TEST";
        final GenreRs entity = service.get(3L);
        final String oldName = entity.name();
        final GenreRs newEntity = service.update(3L, new GenreRq(test));

        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals(test, newEntity.name());
        Assertions.assertNotEquals(oldName, newEntity.name());

        final GenreRs cmpEntity = service.get(3L);
        Assertions.assertEquals(newEntity.id(), cmpEntity.id());
        Assertions.assertEquals(newEntity.name(), cmpEntity.name());
    }

    @Test
    @Order(3)
    void deleteTest() {
        service.delete(3L);
        Assertions.assertEquals(2, service.getAll().size());

        final GenreRs last = service.get(2L);
        Assertions.assertEquals(2L, last.id());

        final GenreRs newEntity = service.create(new GenreRq("Хэви-метал"));
        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals(4L, newEntity.id());
    }
}