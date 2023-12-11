package com.jpoverclock.aoc;

import java.math.BigInteger;
import java.util.*;

public class Day11 {

    record Point(int x, int y) {
        public long manhattanDistance(Point other) {
            return Math.abs(other.x - x) + Math.abs(other.y - y);
        }
    }

    static class GalaxyMap {
        private final char[][] map;
        private final int width;
        private final int height;

        private GalaxyMap(char[][] map, int width, int height) {
            this.map = map;
            this.width = width;
            this.height = height;
        }

        public BigInteger shortestPaths(BigInteger emptyLineMultiplier) {
            Set<Integer> filledRows = new HashSet<>();
            Set<Integer> filledColumns = new HashSet<>();
            List<Point> galaxies = new ArrayList<>();
            BigInteger sum = BigInteger.ZERO;

            // Traverse the map to get all galaxies
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (map[y][x] == '#') {
                        filledRows.add(y);
                        filledColumns.add(x);
                        galaxies.add(new Point(x, y));
                    }
                }
            }

            // Find distances between each galaxy
            for (int i = 0; i < galaxies.size(); i++) {
                var galaxy = galaxies.get(i);

                for (int j = i + 1; j < galaxies.size(); j++) {
                    var otherGalaxy = galaxies.get(j);
                    long distance = otherGalaxy.manhattanDistance(galaxy);

                    // Empty space between galaxies doubles the distance
                    long emptyLines = 0;
                    for (int x = Math.min(galaxy.x, otherGalaxy.x); x <= Math.max(galaxy.x, otherGalaxy.x); x++) {
                        if (!filledColumns.contains(x)) emptyLines++;
                    }

                    for (int y = Math.min(galaxy.y, otherGalaxy.y); y <= Math.max(galaxy.y, otherGalaxy.y); y++) {
                        if (!filledRows.contains(y)) emptyLines++;
                    }

                    sum = sum.add(BigInteger.valueOf(distance)).add(BigInteger.valueOf(emptyLines).multiply(emptyLineMultiplier));
                }
            }

            return sum;
        }

        public static GalaxyMap parseInput(String input) {
            String[] lines = input.split("\n");
            char[][] map = new char[lines.length][];

            for (int i = 0; i < lines.length; i++) {
                char[] chars = lines[i].toCharArray();
                map[i] = new char[chars.length];

                System.arraycopy(chars, 0, map[i], 0, chars.length);
            }

            return new GalaxyMap(map, map[0].length, map.length);
        }
    }

    public static String part1(String input) {
        GalaxyMap map = GalaxyMap.parseInput(input);
        BigInteger sumOfDistances = map.shortestPaths(BigInteger.ONE);

        return sumOfDistances.toString();
    }

    public static String part2(String input, BigInteger multiplier) {
        GalaxyMap map = GalaxyMap.parseInput(input);
        BigInteger sumOfDistances = map.shortestPaths(multiplier);

        return sumOfDistances.toString();
    }
}
