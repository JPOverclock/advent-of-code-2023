package com.jpoverclock.aoc;

import java.util.HashMap;
import java.util.Map;

public class Day14 {

    static class Platform {

        private final int height;

        private final int width;

        private final char[][] map;

        public Platform(int width, int height, char[][] map) {
            this.width = width;
            this.height = height;
            this.map = map;
        }

        public String hash() {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    builder.append(map[i][j]);
                }
            }

            return builder.toString();
        }

        public void tiltNorth() {
            // Column by column
            for (int j = 0; j < width; j++) {
                int anchor = 0;
                for (int i = 0; i < height; i++) {
                    switch (map[i][j]) {
                        case '#' -> anchor = i + 1; // Reset anchor
                        case 'O' -> {
                            map[anchor][j] = 'O';
                            if (anchor != i) map[i][j] = '.';
                            anchor++;
                        }
                    }
                }
            }
        }

        public void tiltSouth() {
            // Column by column
            for (int j = 0; j < width; j++) {
                int anchor = height - 1;
                for (int i = height - 1; i >= 0; i--) {
                    switch (map[i][j]) {
                        case '#' -> anchor = i - 1; // Reset anchor
                        case 'O' -> {
                            map[anchor][j] = 'O';
                            if (anchor != i) map[i][j] = '.';
                            anchor--;
                        }
                    }
                }
            }
        }

        public void tiltEast() {
            // Row by row
            for (int i = 0; i < height; i++) {
                int anchor = width - 1;
                for (int j = width - 1; j >= 0; j--) {
                    switch (map[i][j]) {
                        case '#' -> anchor = j - 1; // Reset anchor
                        case 'O' -> {
                            map[i][anchor] = 'O';
                            if (anchor != j) map[i][j] = '.';
                            anchor--;
                        }
                    }
                }
            }
        }

        public void tiltWest() {
            // Row by row
            for (int i = 0; i < height; i++) {
                int anchor = 0;
                for (int j = 0; j < width; j++) {
                    switch (map[i][j]) {
                        case '#' -> anchor = j + 1; // Reset anchor
                        case 'O' -> {
                            map[i][anchor] = 'O';
                            if (anchor != j) map[i][j] = '.';
                            anchor++;
                        }
                    }
                }
            }
        }

        public void cycle() {
            tiltNorth();
            tiltWest();
            tiltSouth();
            tiltEast();
        }

        public void debugPrint() {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    System.out.print(map[i][j]);
                }

                System.out.println();
            }
        }

        public long computeLoad() {
            long sum = 0L;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (map[i][j] == 'O') sum += (height - i);
                }
            }

            return sum;
        }

        public static Platform valueOf(String value) {
            String[] lines = value.split("\n");

            int height = lines.length;
            int width = lines[0].length();

            char[][] map = new char[height][width];

            for (int i = 0; i < height; i++) {
                char[] contents = lines[i].toCharArray();
                System.arraycopy(contents, 0, map[i], 0, width);
            }

            return new Platform(width, height, map);
        }
    }

    public static String part1(String input) {
        Platform platform = Platform.valueOf(input);
        System.out.println("BEFORE:");
        platform.debugPrint();

        platform.tiltNorth();

        System.out.println();
        System.out.println("AFTER:");
        platform.debugPrint();

        long load = platform.computeLoad();
        return Long.toString(load);
    }

    public static String part2(String input, long iterations) {
        Platform platform = Platform.valueOf(input);

        Map<String, Long> configurations = new HashMap<>();

        for (long i = 0; i < iterations; i++) {
            var hash = platform.hash();

            if (configurations.containsKey(hash)) {
                // Cycle was detected
                // Compute how many iterations are required
                long period = i - configurations.get(hash);
                long remaining = (iterations - i);
                long additional = remaining % period;

                iterations = i + additional;
            } else {
                configurations.put(platform.hash(), i);
            }

            platform.cycle();
        }

        return Long.toString(platform.computeLoad());
    }
}
