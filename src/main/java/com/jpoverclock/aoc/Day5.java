package com.jpoverclock.aoc;

import java.util.*;

public class Day5 {

    private final String input;

    record NumberRange(long start, long end) { }

    record ConvertRangeResult(NumberRange inside, List<NumberRange> outside) { }

    record Range(long destinationStart, long sourceStart, long size) {

        public boolean isSourceInRange(long number) {
            return number >= sourceStart && number < (sourceStart + size);
        }

        public long convertSource(long number) {
            long distance = number - sourceStart;
            return destinationStart + distance;
        }

        public ConvertRangeResult convertSourceRange(NumberRange range) {
            // Perform axis intersection
            long a = range.start;
            long b = range.end;
            long c = sourceStart;
            long d = sourceStart + (size - 1);

            if (b < c || a > d) {
                // a--b      a--b
                //      c--d
                return new ConvertRangeResult(null, List.of(new NumberRange(a, b))); // Maps to itself
            } else if (a < c && b <= d) {
                //  a-----b
                //     c----d
                return new ConvertRangeResult(new NumberRange(convertSource(c), convertSource(b)), List.of(new NumberRange(a, c - 1)));
            } else if (a >= c && b > d) {
                //      a-----b
                //    c-----d
                return new ConvertRangeResult(new NumberRange(convertSource(a), convertSource(d)), List.of(new NumberRange(d + 1, b)));
            } else if (a < c && b > d) {
                //  a---------b
                //    c-----d
                return new ConvertRangeResult(new NumberRange(convertSource(c), convertSource(d)), List.of(new NumberRange(a, c - 1), new NumberRange(d + 1, b)));
            } else {
                //    a--b
                // c--------d
                return new ConvertRangeResult(new NumberRange(convertSource(a), convertSource(b)), null);
            }
        }

        public static Range fromString(String value) {
            String[] parts = value.trim().split("\\s+");
            return new Range(
                    Long.parseLong(parts[0]),
                    Long.parseLong(parts[1]),
                    Long.parseLong(parts[2])
            );
        }
    }

    record Mapping(String from, String to, List<Range> ranges) {

        long convert(long source) {
            for (var range : ranges) {
                if (range.isSourceInRange(source))
                    return range.convertSource(source);
            }

            return source;
        }

        List<NumberRange> convert(List<NumberRange> numRanges) {
            List<NumberRange> convertedRanges = new LinkedList<>();
            List<NumberRange> workingRanges = new LinkedList<>(numRanges);

            for (var range : ranges) {
                List<NumberRange> nextRanges = new LinkedList<>();
                for (var numRange : workingRanges) {
                    ConvertRangeResult result = range.convertSourceRange(numRange);

                    if (result.inside != null) {
                        convertedRanges.add(result.inside);
                    }

                    if (result.outside != null) {
                        nextRanges.addAll(result.outside);
                    }
                }
                workingRanges = nextRanges;
            }

            convertedRanges.addAll(workingRanges);
            return convertedRanges;
        }

        public static Mapping fromString(String value) {
            String[] parts = value.split("\n");

            // First line contains the names
            String[] names = parts[0].trim().replace(" map:", "").split("-to-");

            // Subsequent lines contain the ranges
            List<Range> ranges = new LinkedList<>();

            for (int i = 1; i < parts.length; i++) {
                ranges.add(Range.fromString(parts[i]));
            }

            return new Mapping(names[0], names[1], ranges);
        }
    }

    public Day5(String input) {
        this.input = input;
    }

    public String part1() {

        String[] parts = input.split("\n\n");

        // Seeds are the first line
        String[] seedValues = parts[0].replace("seeds: ", "")
                .trim()
                .split("\\s+");

        List<Long> seeds = new ArrayList<>(seedValues.length);

        for (var seedValue : seedValues) {
            seeds.add(Long.parseLong(seedValue));
        }

        // Then we build a pipeline of mappings
        List<Mapping> mappings = new LinkedList<>();

        for (int i = 1; i < parts.length; i++) {
            mappings.add(Mapping.fromString(parts[i]));
        }

        /* Finally, we just pass the seeds through the mappings and
            check which value we get at the end  */

        long lowest = Long.MAX_VALUE;

        for (long seed : seeds) {
            long value = seed;

            for (var mapping : mappings) {
                value = mapping.convert(value);
            }

            lowest = Math.min(lowest, value);
        }

        return String.valueOf(lowest);
    }

    public String part2() {
        // Naive implementation
        String[] parts = input.split("\n\n");

        // Seeds are the first line
        String[] seedValues = parts[0].replace("seeds: ", "")
                .trim()
                .split("\\s+");

        // Each pair represents a range now
        List<NumberRange> ranges = new LinkedList<>();

        for (int i = 0; i < seedValues.length - 1; i += 2) {
            long start = Long.parseLong(seedValues[i]);
            long length = Long.parseLong(seedValues[i + 1]);

            ranges.add(new NumberRange(start, start + (length - 1)));
        }

        // Then we build a pipeline of mappings
        List<Mapping> mappings = new LinkedList<>();

        for (int i = 1; i < parts.length; i++) {
            mappings.add(Mapping.fromString(parts[i]));
        }

        /* Finally, we just pass the seeds through the mappings and
            check which value we get at the end  */
        for (Mapping mapping : mappings) {
            ranges = mapping.convert(ranges);
        }

        // What's the lowest lower bound in the output ranges?
        long lowest = Long.MAX_VALUE;

        for (var range : ranges) {
            if (range.start < lowest) lowest = range.start;
        }

        return String.valueOf(lowest);
    }
}
