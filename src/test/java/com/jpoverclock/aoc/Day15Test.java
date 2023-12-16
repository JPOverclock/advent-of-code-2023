package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day15Test {

    private static final String EXAMPLE_INPUT = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

    @Test
    void part1() {
        assertEquals("1320", Day15.part1(EXAMPLE_INPUT));
    }

    @Test
    void part2() {
        assertEquals("145", Day15.part2(EXAMPLE_INPUT));
    }
}