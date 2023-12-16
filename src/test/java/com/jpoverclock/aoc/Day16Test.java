package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day16Test {

    private static final String EXAMPLE_INPUT = """
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....
            """;

    @Test
    void part1() {
        assertEquals("46", Day16.part1(EXAMPLE_INPUT));
    }

    @Test
    void part2() {
        assertEquals("51", Day16.part2(EXAMPLE_INPUT));
    }
}