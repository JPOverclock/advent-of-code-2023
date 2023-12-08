package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day7Test {

    private final String EXAMPLE_INPUT = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;

    @Test
    void part1() {
        assertEquals("6440", new Day7(EXAMPLE_INPUT).part1());
    }

    @Test
    void part2() {
        assertEquals("5905", new Day7(EXAMPLE_INPUT).part2());
    }
}