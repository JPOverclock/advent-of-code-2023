package com.jpoverclock.aoc;

import java.math.BigInteger;
import java.util.*;

public class Day3 {

    private static final Point[] NEIGHBORS = {
            new Point(-1, 0),
            new Point(-1, -1),
            new Point(-1, 1),
            new Point(0, -1),
            new Point(0, 1),
            new Point(1, -1),
            new Point(1, 0),
            new Point(1, 1)
    };

    private final Map<Point, Part> partsMap = new HashMap<>();
    private final List<Symbol> symbols = new ArrayList<>();

    record Part(int number, List<Point> points) { }

    record Point(int i, int j) {
        Point add(Point other) {
            return new Point(i + other.i, j + other.j);
        }
    }

    public Day3(String input) {
        // Parse input
        String[] lines = input.split("\n");

        String number = "";
        List<Point> numberPoints = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);

                if (c >= '0' && c <= '9') {
                    number += c;
                    numberPoints.add(new Point(i, j));
                } else {
                    if (!numberPoints.isEmpty()) {
                        List<Point> partPoints = new ArrayList<>(numberPoints);

                        var part = new Part(Integer.parseInt(number), partPoints);

                        for (var point : numberPoints) {
                            partsMap.put(point, part);
                        }

                        number = "";
                        numberPoints.clear();
                    }

                    if (c != '.') {
                        // Number is a symbol
                        symbols.add(new Symbol(new Point(i, j), c));
                    }

                }
            }
        }
    }

    record Symbol(Point location, Character symbol) { }

    public String part1() {
        // Calculate the sum along the symbols
        Set<Part> parts = new HashSet<>();

        for (var symbol : symbols) {
            parts.addAll(partsNextTo(symbol.location));
        }

        long sum = parts.stream().mapToLong(Part::number).sum();

        //Find the location of numbers
        return Long.toString(sum);
    }

    public String part2() {
        BigInteger result = BigInteger.ZERO;

        for (var symbol : symbols) {
            if (symbol.symbol.equals('*')) {
                var parts = partsNextTo(symbol.location);

                if (parts.size() == 2) {
                    long[] numbers = parts.stream().mapToLong(Part::number).toArray();

                    result = result.add(new BigInteger(Long.toString(numbers[0] * numbers[1])));
                }
            }
        }

        return result.toString();
    }

    private Set<Part> partsNextTo(Point point) {
        Set<Part> parts = new HashSet<>();

        for (var neighbor : NEIGHBORS) {
            var partPoint = point.add(neighbor);
            if (partsMap.containsKey(partPoint)) {
                parts.add(partsMap.get(partPoint));
            }
        }

        return parts;
    }
}
