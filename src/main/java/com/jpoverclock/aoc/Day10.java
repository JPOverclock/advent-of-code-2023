package com.jpoverclock.aoc;

import java.util.*;

public class Day10 {

    record Point(int x, int y) {
        public Point add(Point other) {
            return new Point(x + other.x, y + other.y);
        }
    }

    private static final Point NORTH = new Point(0, -1);
    private static final Point SOUTH = new Point(0, 1);
    private static final Point EAST = new Point(1, 0);
    private static final Point WEST = new Point(-1, 0);

    private static final Map<Character, Set<Point>> NAVIGATION = Map.ofEntries(
            Map.entry('S', Set.of(NORTH, SOUTH, EAST, WEST)),
            Map.entry('|', Set.of(NORTH, SOUTH)),
            Map.entry('-', Set.of(EAST, WEST)),
            Map.entry('L', Set.of(NORTH, EAST)),
            Map.entry('J', Set.of(NORTH, WEST)),
            Map.entry('7', Set.of(SOUTH, WEST)),
            Map.entry('F', Set.of(SOUTH, EAST)),
            Map.entry('.', Set.of())
    );

    static class Maze {

        public final char[][] map;

        public Maze(char[][] map) {
            this.map = map;
        }

        public Map<Point, Integer> computeLoopDistances() {
            Map<Point, Integer> distances = new HashMap<>();

            // Find the starting position
            Point startingPosition = null;

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] == 'S')
                        startingPosition = new Point(j, i);
                }
            }

            // Build loop
            List<Point> frontier = new LinkedList<>();
            distances.put(startingPosition, 0);
            frontier.add(startingPosition);

            while (!frontier.isEmpty()) {
                List<Point> next = new LinkedList<>();

                for (var point : frontier) {
                    // Resolve neighbours
                    Set<Point> possibleDirections = NAVIGATION.get(map[point.y][point.x]);
                    Set<Point> actualNeighbours = new HashSet<>();

                    for (var possibleDirection : possibleDirections) {
                        Point adjacentPoint = point.add(possibleDirection);

                        // If point is outside the map, discard
                        if (adjacentPoint.x < 0 ||
                                adjacentPoint.y < 0 ||
                                adjacentPoint.x >= map[0].length ||
                                adjacentPoint.y >= map.length
                        ) continue;

                        // If the adjacent point already exists in our distances map, discard
                        if (distances.containsKey(adjacentPoint)) continue;

                        // If the adjacent point doesn't connect back to this point, discard
                        boolean connects = false;
                        char adjacent = map[adjacentPoint.y][adjacentPoint.x];

                        if (possibleDirection.equals(NORTH)) {
                            connects = NAVIGATION.get(adjacent).contains(SOUTH);
                        } else if (possibleDirection.equals(SOUTH)) {
                            connects = NAVIGATION.get(adjacent).contains(NORTH);
                        } else if (possibleDirection.equals(EAST)) {
                            connects = NAVIGATION.get(adjacent).contains(WEST);
                        } else if (possibleDirection.equals(WEST)) {
                            connects = NAVIGATION.get(adjacent).contains(EAST);
                        }

                        if (connects) actualNeighbours.add(adjacentPoint);
                    }

                    // Add all actual neighbours to next
                    for (var neighbor : actualNeighbours) {
                        distances.put(neighbor, distances.get(point) + 1);
                        next.add(neighbor);
                    }
                }

                frontier = next;
            }

            return distances;
        }
    }

    private static char[][] parseInput(String input) {
        String[] lines = input.split("\n");

        char[][] map = new char[lines.length][];

        for (int i = 0; i < lines.length; i++) {
            char[] chars = lines[i].toCharArray();
            map[i] = new char[chars.length];

            System.arraycopy(chars, 0, map[i], 0, chars.length);
        }

        return map;
    }

    public static String part1(String input) {
        var maze = new Maze(parseInput(input));
        Map<Point, Integer> distances = maze.computeLoopDistances();

        return Integer.toString(
                distances.values().stream()
                        .mapToInt(i -> i)
                        .max()
                        .orElse(0)
        );
    }

    public static String part2(String input) {
        var maze = new Maze(parseInput(input));
        Map<Point, Integer> distances = maze.computeLoopDistances();

        // What's the loop's bounding box
        int minX = maze.map[0].length;
        int maxX = 0;
        int minY = maze.map.length;
        int maxY = 0;

        for (var point : distances.keySet()) {
            if (point.x > maxX) maxX = point.x;
            if (point.x < minX) minX = point.x;
            if (point.y > maxY) maxY = point.y;
            if (point.y < minY) minY = point.y;
        }

        // Scan left to right
        int count = 0;

        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                // Perform ray casting algorithm if point does not belong to edges
                if (!distances.containsKey(new Point(j, i))) {
                    int intersections = 0;

                    char inEdgeOf = '*';
                    boolean inEdge = false;


                    for (int k = j + 1; k <= maxX; k++) {
                        Point p = new Point(k, i);

                        if (distances.containsKey(p)) {
                            // Intersected, but we have to check individual cases...
                            // We count F---J and L---7 as one crossing
                            // We don't count F---7 and L----J
                            switch (maze.map[i][k]) {
                                case '|' -> intersections++;
                                case 'F', 'L' -> {
                                    if (!inEdge) {
                                        inEdge = true;
                                        inEdgeOf = maze.map[i][k];
                                    }
                                }
                                case 'J' -> {
                                    if (inEdge && inEdgeOf == 'F') {
                                        inEdge = false;
                                        inEdgeOf = '*';
                                        intersections++;
                                    } else if (inEdge && inEdgeOf == 'L') {
                                        inEdge = false;
                                        inEdgeOf = '*';
                                        // Do not count
                                    }
                                }
                                case '7' -> {
                                    if (inEdge && inEdgeOf == 'L') {
                                        inEdge = false;
                                        inEdgeOf = '*';
                                        intersections++;
                                    } else if (inEdge && inEdgeOf == 'F') {
                                        inEdge = false;
                                        inEdgeOf = '*';
                                    }
                                }
                            }
                        }
                    }

                    // If intersections is odd, point is inside
                    count += (intersections % 2);
                }
            }
        }

        return Integer.toString(count);
    }
}
