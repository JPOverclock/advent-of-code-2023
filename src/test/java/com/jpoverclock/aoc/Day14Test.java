package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day14Test {

    private static final String EXAMPLE_INPUT = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
            """;

    @Test
    void part1() {
        assertEquals("136", Day14.part1(EXAMPLE_INPUT));
    }

    @Test
    void part2() {
        assertEquals("64", Day14.part2(EXAMPLE_INPUT, 1_000_000_000));
    }
}