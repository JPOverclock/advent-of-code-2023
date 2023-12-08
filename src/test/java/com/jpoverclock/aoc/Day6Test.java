package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day6Test {

    private static final String EXAMPLE_INPUT = """
            Time:      7  15   30
            Distance:  9  40  200
            """;

    @Test
    void part1() {
        assertEquals("288", new Day6(EXAMPLE_INPUT).part1());
    }

    @Test
    void part2() {
        assertEquals("71503", new Day6(EXAMPLE_INPUT).part2());
    }
}