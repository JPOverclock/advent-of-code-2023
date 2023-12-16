package com.jpoverclock.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day12Test {

    private static final String EXAMPLE_INPUT = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """;

    private static final String SINGLE_EXAMPLE = "?###???????? 3,2,1";

    private static final String GREEDY_TEST = "??#??????#?#??..?.. 2,3,1";

    private static final String SINGLE_WRONG_ANSWER = "????.#...#... 4,1,1";

    @Test
    void part1() {
        assertEquals("21", Day12.part1(EXAMPLE_INPUT));
    }

    @Test
    void part2() {
        assertEquals("525152", Day12.part2(EXAMPLE_INPUT));
    }
}