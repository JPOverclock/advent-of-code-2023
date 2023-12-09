package com.jpoverclock.aoc;

import java.util.Arrays;
import java.util.stream.Stream;

public class Day9 {

    private static int lastDigitOf(int[] numbers) {
        if (Arrays.stream(numbers).allMatch(i -> i == 0)) return 0;

        int[] next = new int[numbers.length - 1];

        for (int i = 0, k = 0; i < numbers.length - 1; i++) {
            next[k++] = numbers[i + 1] - numbers[i];
        }

        return numbers[numbers.length - 1] + lastDigitOf(next);
    }

    private static int firstDigitOf(int[] numbers) {
        if (Arrays.stream(numbers).allMatch(i -> i == 0)) return 0;

        int[] next = new int[numbers.length - 1];

        for (int i = 0, k = 0; i < numbers.length - 1; i++) {
            next[k++] = numbers[i + 1] - numbers[i];
        }

        return numbers[0] - firstDigitOf(next);
    }

    private static Stream<int[]> parseInput(String input) {
        return Arrays.stream(input.split("\n"))
                .map(line -> Arrays.stream(line.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray()
                );
    }

    public static String part1(String input) {
        long result = parseInput(input)
                .map(Day9::lastDigitOf).mapToLong(i -> i)
                .sum();

        return Long.toString(result);
    }

    public static String part2(String input) {
        long result = parseInput(input)
                .map(Day9::firstDigitOf).mapToLong(i -> i)
                .sum();

        return Long.toString(result);
    }
}
