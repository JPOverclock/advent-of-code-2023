package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day3Test {

    private static final String EXAMPLE_INPUT = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """;

    @Test
    public void testPart1() {
        assertEquals("4361", new Day3(EXAMPLE_INPUT).part1());
    }

    @Test
    public void testPart2() {
        assertEquals("467835", new Day3(EXAMPLE_INPUT).part2());
    }

}