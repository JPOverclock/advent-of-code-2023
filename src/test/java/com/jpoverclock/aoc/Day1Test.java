package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day1Test {

    private static final String EXAMPLE_PART_1 = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """;

    private static final String EXAMPLE_PART_2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """;

    @Test
    void part1() {
        assertEquals(142, new Day1().part1(EXAMPLE_PART_1));
    }

    @Test
    void part2() {
        assertEquals(281, new Day1().part2(EXAMPLE_PART_2));
    }
}