package com.jpoverclock.aoc;

import java.util.*;

public class Day18 {

    private static final Map<Character, Point> DIRECTIONS = Map.of(
            'U', new Point(0, 1),
            'D', new Point(0, -1),
            'L', new Point(-1, 0),
            'R', new Point(1, 0)
    );

    public record Instruction(char direction, int count, int color) {
        public static Instruction valueOf(String value) {
            String[] parts = value.split("\\s+");

            char direction = parts[0].charAt(0);
            int count = Integer.parseInt(parts[1]);
            int color = Integer.parseInt(parts[2].substring(2, parts[2].length() - 1), 16);

            return new Instruction(direction, count, color);
        }

        public static Instruction valueOfHex(String value) {
            String[] parts = value.split("\\s+");
            String hex = parts[2].substring(2, parts[2].length() - 1);

            char direction = switch (hex.substring(hex.length() - 1)) {
                case "0" -> 'R';
                case "1" -> 'D';
                case "2" -> 'L';
                case "3" -> 'U';
                default -> throw new RuntimeException("Undefined direction!");
            };

            int count = Integer.parseInt(hex.substring(0, hex.length() - 1), 16);

            return new Instruction(direction, count, Integer.parseInt(hex, 16));
        }
    }

    public record Point(long x, long y) {
        public Point add(Point other) {
            return new Point(x + other.x, y + other.y);
        }
    }

    public record Edge(Point a, Point b) {
        long length() {
            return Math.abs((b.x - a.x) + (b.y - a.y));
        }
    }

    private static Long area(List<Instruction> instructions) {
        List<Edge> edges = new LinkedList<>();
        List<Point> polyPoints = new ArrayList<>();

        // Follow instructions
        Point cursor = new Point(0, 0);
        polyPoints.add(cursor);

        System.out.println("Following instructions...");

        for (var instruction : instructions) {
            System.out.println("\t" + instruction);
            var direction = DIRECTIONS.get(instruction.direction);
            var start = cursor;
            var end = start.add(new Point(direction.x * instruction.count, direction.y * instruction.count));

            edges.add(new Edge(start, end));
            polyPoints.add(end);
            cursor = end;
        }

        // Shoelace algorithm
        long sum = 0;

        // Transpose points?
        for (int i = 0; i < polyPoints.size() - 1; i++) {
            Point a = polyPoints.get(i);
            Point b = polyPoints.get(i + 1);

            long det = (a.x * b.y) - (a.y * b.x);
            sum += det;
        }

        return (edges.stream().mapToLong(Edge::length).sum() / 2) + (Math.abs(sum) / 2) + 1;
    }

    public static String part1(String input) {
        List<Instruction> instructions = Arrays.stream(input.split("\n"))
                .map(Instruction::valueOf)
                .toList();

        return Long.toString(area(instructions));
    }

    public static String part2(String input) {
        List<Instruction> instructions = Arrays.stream(input.split("\n"))
                .map(Instruction::valueOfHex)
                .toList();

        return Long.toString(area(instructions));
    }
}
