
package ru.ulstu.is.server.api;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public record PageRs<D>(List<D> data, Integer pages, Long items) {

    public static <E, D> PageRs<D> fromPage(
            Page<E> page, Function<E, D> converter) {

        var data = page.getContent().stream()
                .map(converter)
                .toList();

        return new PageRs<>(
                data,
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
