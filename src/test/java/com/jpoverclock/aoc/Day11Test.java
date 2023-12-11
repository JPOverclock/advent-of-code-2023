package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class Day11Test {

    private static final String EXAMPLE_INPUT = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """;

    @Test
    void part1() {
        assertEquals("374", Day11.part1(EXAMPLE_INPUT));
    }

    @Test
    void part2tenX() {
        assertEquals("1030", Day11.part2(EXAMPLE_INPUT, BigInteger.valueOf(9L)));
    }

    @Test
    void part2hundredX() {
        assertEquals("8410", Day11.part2(EXAMPLE_INPUT, BigInteger.valueOf(99L)));
    }
}