package com.jpoverclock.aoc;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {

    private static final Map<String, String> WORD_TO_DIGIT = Map.ofEntries(
            Map.entry("one", "1"),
            Map.entry("two", "2"),
            Map.entry("three", "3"),
            Map.entry("four", "4"),
            Map.entry("five", "5"),
            Map.entry("six", "6"),
            Map.entry("seven", "7"),
            Map.entry("eight", "8"),
            Map.entry("nine", "9")
    );

    public long part1(String input) {
        return sumEntries(input, "(\\d)", s -> s);
    }

    public long part2(String input) {
        return sumEntries(input, "(\\d|one|two|three|four|five|six|seven|eight|nine)", this::convertToNumber);
    }

    private long sumEntries(String input, String regex, Function<String, String> converter) {
        var pattern = Pattern.compile(regex);

        String[] lines = input.split("\n");
        long sum = 0L;

        for (var line : lines) {
            Matcher matcher = pattern.matcher(line);
            List<String> matches = new LinkedList<>();

            while (matcher.find()) {
                matches.add(matcher.group());
            }

            String first = converter.apply(matches.get(0));
            String last = converter.apply(matches.get(matches.size() - 1));

            sum += Long.parseLong(first + last);
        }

        return sum;
    }

    private String convertToNumber(String value) {
        return WORD_TO_DIGIT.getOrDefault(value, value);
    }
}
