package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test {

    private static final String EXAMPLE_SIMPLE = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
            """;

    private static final String EXAMPLE_SIMPLE_NON_CONNECT = """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF
            """;

    private static final String EXAMPLE_MORE_COMPLEX = """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
            """;

    private static final String EXAMPLE_MORE_COMPLEX_NON_CONNECT = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
            """;

    private static final String EXAMPLE_PART_2_SIMPLE = """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
            """;

    private static final String EXAMPLE_PART_2_SQUEEZE = """
            ..........
            .S------7.
            .|F----7|.
            .||....||.
            .||....||.
            .|L-7F-J|.
            .|..||..|.
            .L--JL--J.
            ..........
            """;

    private static final String EXAMPLE_PART_2_COMPLEX = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
            """;

    private static final String EXAMPLE_PART_2_MORE_COMPLEX = """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
            """;

    @Test
    void part1Simple() {
        assertEquals("4", Day10.part1(EXAMPLE_SIMPLE));
    }

    @Test
    void part1SimpleNonConnect() {
        assertEquals("4", Day10.part1(EXAMPLE_SIMPLE_NON_CONNECT));
    }

    @Test
    void part1Complex() {
        assertEquals("8", Day10.part1(EXAMPLE_MORE_COMPLEX));
    }

    @Test
    void part1ComplexNonConnect() {
        assertEquals("8", Day10.part1(EXAMPLE_MORE_COMPLEX_NON_CONNECT));
    }

    @Test
    void part2Simple() {
        assertEquals("4", Day10.part2(EXAMPLE_PART_2_SIMPLE));
    }

    @Test
    void part2Squeeze() {
        assertEquals("4", Day10.part2(EXAMPLE_PART_2_SQUEEZE));
    }

    @Test
    void part2Complex() {
        assertEquals("8", Day10.part2(EXAMPLE_PART_2_COMPLEX));
    }

    @Test
    void part2MoreComplex() {
        assertEquals("10", Day10.part2(EXAMPLE_PART_2_MORE_COMPLEX));
    }
}