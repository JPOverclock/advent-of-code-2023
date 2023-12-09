package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day9Test {

    private static final String EXAMPLE_INPUT = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;

    @Test
    void part1() {
        assertEquals("114", Day9.part1(EXAMPLE_INPUT));
    }

    @Test
    void part2() {
        assertEquals("2", Day9.part2(EXAMPLE_INPUT));
    }
}