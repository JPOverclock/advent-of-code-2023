package com.jpoverclock.aoc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Day13 {

    record ValleyMap(char[][] map) {

        public int reflectionValue() {
            // Vertical first...
            for (int j = 0; j < map[0].length - 1; j++) {
                if (isVerticalReflection(j, j + 1)) return j + 1;
            }

            // ... then try horizontal
            for (int i = 0; i < map.length - 1; i++) {
                if (isHorizontalReflection(i, i + 1)) return (i + 1) * 100;
            }

            return -1;
        }

        public Set<Integer> reflectionValues() {
            Set<Integer> values = new HashSet<>();

            // Vertical first...
            for (int j = 0; j < map[0].length - 1; j++) {
                if (isVerticalReflection(j, j + 1)) {
                    values.add(j + 1);
                }
            }

            // ... then try horizontal
            for (int i = 0; i < map.length - 1; i++) {
                if (isHorizontalReflection(i, i + 1)) {
                    values.add((i + 1) * 100);
                }
            }

            return values;
        }

        public int reflectionValueSmudged() {
            int originalReflectionValue = reflectionValue();

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    char original = map[i][j];
                    map[i][j] = original == '#' ? '.' : '#';

                    Set<Integer> values = reflectionValues();
                    values.remove(originalReflectionValue);

                    if (!values.isEmpty()) {
                        return values.stream().findFirst().orElse(-1);
                    }

                    map[i][j] = original;
                }
            }

            return -1;
        }

        private boolean isVerticalReflection(int j1, int j2) {
            if (j1 < 0 || j2 >= map[0].length) return true;

            for (int i = 0; i < map.length; i++) {
                if (map[i][j1] != map[i][j2]) return false;
            }

            return isVerticalReflection(j1 -1, j2 + 1);
        }

        private boolean isHorizontalReflection(int i1, int i2) {
            if (i1 < 0 || i2 >= map.length) return true;

            for (int j = 0; j < map[0].length; j++) {
                if (map[i1][j] != map[i2][j]) return false;
            }

            return isHorizontalReflection(i1 -1, i2 + 1);
        }

        public static ValleyMap valueOf(String value) {
            String[] lines = value.split("\n");
            char[][] map = new char[lines.length][];

            for (int i = 0; i < lines.length; i++) {
                map[i] = Arrays.copyOf(lines[i].toCharArray(), lines[i].length());
            }

            return new ValleyMap(map);
        }

        public static Stream<ValleyMap> parseInput(String input) {
            String[] blocks = input.split("\n\n");
            return Arrays.stream(blocks).map(ValleyMap::valueOf);
        }
    }

    public static String part1(String input) {
        long result = ValleyMap.parseInput(input).mapToLong(ValleyMap::reflectionValue).sum();
        return Long.toString(result);
    }

    public static String part2(String input) {
        long result = ValleyMap.parseInput(input).mapToLong(ValleyMap::reflectionValueSmudged).sum();
        return Long.toString(result);
    }
}
