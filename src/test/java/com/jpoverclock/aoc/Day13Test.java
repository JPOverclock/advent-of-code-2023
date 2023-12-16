package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test {

    private static final String EXAMPLE_INPUT = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
                        
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
            """;

    @Test
    void part1() {
        assertEquals("405", Day13.part1(EXAMPLE_INPUT));
    }

    @Test
    void part2() {
        assertEquals("400", Day13.part2(EXAMPLE_INPUT));
    }
}