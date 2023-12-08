package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

    private static final String EXAMPLE_1 = """
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """;

    private static final String EXAMPLE_2 = """
            LLR
                        
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
            """;

    private static final String EXAMPLE_3 = """
            LR
                        
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
            """;

    @Test
    void part1Example1() {
        assertEquals("2", new Day8(EXAMPLE_1).part1());
    }

    @Test
    void part1Example2() {
        assertEquals("6", new Day8(EXAMPLE_2).part1());
    }

    @Test
    void part2Example3() {
        assertEquals("6", new Day8(EXAMPLE_3).part2());
    }
}