package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Stream;

@RestController
@RequestMapping("/calculation")
public class CalculationController {

    private static final Integer SUM = Stream.iterate(1, a -> a + 1)
            .parallel()
            .limit(1_000_000)
            .reduce(0, Integer::sum);

    @GetMapping("/sum")
    public Integer calculateSum() {
        return SUM;
    }
}