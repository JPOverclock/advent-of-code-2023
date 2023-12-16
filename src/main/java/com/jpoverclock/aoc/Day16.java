package com.jpoverclock.aoc;

import java.util.*;

public class Day16 {

    public record Point(int i, int j) {
        Point add(Point other) {
            return new Point(i + other.i, j + other.j);
        }
    }

    public static class Beam {
        private Point position;
        private Point direction;

        public Beam(Point position, Point direction) {
            this.position = position;
            this.direction = direction;
        }

        public void move() {
            position = position.add(direction);
        }
    }

    private static char[][] parseInput(String input) {
        String[] lines = input.split("\n");
        char[][] grid = new char[lines.length][lines[0].length()];

        for (int i = 0; i < lines.length; i++) {
            grid[i] = Arrays.copyOf(lines[i].toCharArray(), lines[i].length());
        }

        return grid;
    }

    private static int energizeTiles(Beam firstBeam, char[][] grid) {
        Map<Point, Set<Point>> energizedTiles = new HashMap<>();

        Stack<Beam> activeBeams = new Stack<>();
        activeBeams.add(firstBeam);

        while (!activeBeams.isEmpty()) {
            Beam beam = activeBeams.pop();

            // If the beam is outside the grid discard it
            if (beam.position.i == -1 || beam.position.i >= grid.length ||
                    beam.position.j == -1 || beam.position.j >= grid[0].length) continue;

            // If the tile to energize has not been encountered before, add it
            if (!energizedTiles.containsKey(beam.position)) {
                energizedTiles.put(beam.position, new HashSet<>());
            }

            // If the tile has already been energized with the current direction, we can
            // ignore this beam from now on
            if (energizedTiles.get(beam.position).contains(beam.direction)) continue;

            // Energize the tile
            energizedTiles.get(beam.position).add(beam.direction);

            // Now, decide what to do depending on where the beam landed
            List<Beam> splitBeams = new LinkedList<>();

            switch (grid[beam.position.i][beam.position.j]) {
                case '\\' -> {
                    // Flip the beam's direction's coordinates
                    beam.direction = new Point(beam.direction.j, beam.direction.i);
                }
                case '/' -> {
                    // Flip the direction's coordinates and invert the signal
                    beam.direction = new Point(-beam.direction.j, -beam.direction.i);
                }
                case '|' -> {
                    if (beam.direction.j == 1 || beam.direction.j == -1) {
                        // Split the beam
                        splitBeams.add(new Beam(beam.position, new Point(1, 0)));
                        splitBeams.add(new Beam(beam.position, new Point(-1, 0)));
                    }
                }
                case '-' -> {
                    if (beam.direction.i == 1 || beam.direction.i == -1) {
                        // Split the beam
                        splitBeams.add(new Beam(beam.position, new Point(0, 1)));
                        splitBeams.add(new Beam(beam.position, new Point(0, -1)));
                    }
                }
            }

            if (!splitBeams.isEmpty()) {
                // Beam split, push new beams
                for (Beam splitBeam : splitBeams) {
                    splitBeam.move();
                    activeBeams.push(splitBeam);
                }
            } else {
                // Beam continues
                beam.move();
                activeBeams.push(beam);
            }
        }

        return energizedTiles.keySet().size();
    }

    public static String part1(String input) {
        var beam = new Beam(new Point(0, 0), new Point(0, 1));
        int energizedTiles = energizeTiles(beam, parseInput(input));

        return Integer.toString(energizedTiles);
    }

    public static String part2(String input) {
        final char[][] grid = parseInput(input);

        // Create initial stream of beams
        List<Beam> beams = new LinkedList<>();

        // ... first left and right columns
        for (int i = 0; i < grid.length; i++) {
            beams.add(new Beam(new Point(i, 0), new Point(0, 1)));
            beams.add(new Beam(new Point(i, grid[0].length - 1), new Point(0, -1)));
        }

        // ... then the top and bottom rows
        for (int j = 0; j < grid[0].length; j++) {
            beams.add(new Beam(new Point(0, j), new Point(1, 0)));
            beams.add(new Beam(new Point(grid.length - 1, j), new Point(-1, 0)));
        }

        // For each beam, find the max energized tiles
        int max = beams.stream()
                .parallel()
                .mapToInt(beam -> energizeTiles(beam, grid))
                .max()
                .orElse(-1);

        return Integer.toString(max);
    }
}
