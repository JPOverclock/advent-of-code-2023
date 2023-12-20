package com.jpoverclock.aoc;

import java.util.*;
import java.util.stream.Stream;

public class Day17 {

    private static int[][] parseInput(String input) {
        String[] lines = input.split("\n");
        int height = lines.length;
        int width = lines[0].length();

        int[][] grid = new int[height][width];

        for (int i = 0; i < height; i++) {
            char[] chars = lines[i].toCharArray();

            for (int j = 0; j < width; j++) {
                grid[i][j] = chars[j] - '0';
            }
        }

        return grid;
    }

    public record Path(int i, int j, int dI, int dJ, int w, int l, List<Path> visited) {

        @Override
        public String toString() {
            return "Path{" +
                    "i=" + i +
                    ", j=" + j +
                    ", dI=" + dI +
                    ", dJ=" + dJ +
                    ", w=" + w +
                    ", l=" + l +
                    '}';
        }

        public List<Path> next(int[][] grid, int height, int width) {
            List<Path> paths = new LinkedList<>();

            if (i + 1 < height) {
                Path path = new Path(i + 1, j, 1, 0, w + grid[i + 1][j], dI == 1 && dJ == 0 ? l + 1 : 1,
                        Stream.concat(visited.stream(), Stream.of(this)).toList());
                if (path.l < 4 && path.visited.stream().noneMatch(p -> p.i == path.i && p.j == path.j)) paths.add(path);
            }

            if (i - 1 >= 0) {
                Path path = new Path(i - 1, j, -1, 0, w + grid[i - 1][j], dI == -1 && dJ == 0 ? l + 1 : 1,
                        Stream.concat(visited.stream(), Stream.of(this)).toList());
                if (path.l < 4 && path.visited.stream().noneMatch(p -> p.i == path.i && p.j == path.j)) paths.add(path);
            }

            if (j + 1 < width) {
                Path path = new Path(i, j + 1, 0, 1, w + grid[i][j + 1], dI == 0 && dJ == 1 ? l + 1 : 1,
                        Stream.concat(visited.stream(), Stream.of(this)).toList());
                if (path.l < 4 && path.visited.stream().noneMatch(p -> p.i == path.i && p.j == path.j)) paths.add(path);
            }

            if (j - 1 >= 0) {
                Path path = new Path(i, j - 1, 0, -1, w + grid[i][j - 1], dI == 0 && dJ == -1 ? l + 1 : 1, Stream.concat(visited.stream(), Stream.of(this)).toList());
                if (path.l < 4 && path.visited.stream().noneMatch(p -> p.i == path.i && p.j == path.j)) paths.add(path);
            }

            return paths;
        }

        public List<Path> nextUltra(int[][] grid, int height, int width) {
            List<Path> paths = new LinkedList<>();

            if (l < 4) {
                // Must keep going
                int nextI = i + dI;
                int nextJ = j + dJ;

                if (nextI >= 0 && nextJ >= 0 && nextI < height && nextJ < width) {
                    paths.add(new Path(nextI, nextJ, dI, dJ, w + grid[nextI][nextJ], l + 1, Stream.concat(visited.stream(), Stream.of(this)).toList()));
                }

            } else if (l < 10) {
                // Can keep going...
                int nextI = i + dI;
                int nextJ = j + dJ;

                if (nextI >= 0 && nextJ >= 0 && nextI < height && nextJ < width) {
                    paths.add(new Path(nextI, nextJ, dI, dJ, w + grid[nextI][nextJ], l + 1, Stream.concat(visited.stream(), Stream.of(this)).toList()));
                }

                // ... or turn
                int nextDJ = dI;
                int nextDI = dJ;
                nextI = i + nextDI;
                nextJ = j + nextDJ;

                if (nextI >= 0 && nextJ >= 0 && nextI < height && nextJ < width) {
                    paths.add(new Path(nextI, nextJ, nextDI, nextDJ, w + grid[nextI][nextJ], 1, Stream.concat(visited.stream(), Stream.of(this)).toList()));
                }

                nextDJ = -dI;
                nextDI = -dJ;
                nextI = i + nextDI;
                nextJ = j + nextDJ;

                if (nextI >= 0 && nextJ >= 0 && nextI < height && nextJ < width) {
                    paths.add(new Path(nextI, nextJ, nextDI, nextDJ, w + grid[nextI][nextJ], 1, Stream.concat(visited.stream(), Stream.of(this)).toList()));
                }

            } else {
                // Must turn
                int nextDJ = dI;
                int nextDI = dJ;
                int nextI = i + nextDI;
                int nextJ = j + nextDJ;

                if (nextI >= 0 && nextJ >= 0 && nextI < height && nextJ < width) {
                    paths.add(new Path(nextI, nextJ, nextDI, nextDJ, w + grid[nextI][nextJ], 1, Stream.concat(visited.stream(), Stream.of(this)).toList()));
                }

                nextDJ = -dI;
                nextDI = -dJ;
                nextI = i + nextDI;
                nextJ = j + nextDJ;

                if (nextI >= 0 && nextJ >= 0 && nextI < height && nextJ < width) {
                    paths.add(new Path(nextI, nextJ, nextDI, nextDJ, w + grid[nextI][nextJ], 1, Stream.concat(visited.stream(), Stream.of(this)).toList()));
                }
            }

            return paths;
        }
    }

    record PointDirection(int i, int j, int dI, int dJ, int l) { }

    public static String part1(String input) {
        int[][] grid = parseInput(input);
        int height = grid.length;
        int width = grid[0].length;

        PriorityQueue<Path> queue = new PriorityQueue<>(width * height, Comparator.comparingInt(Path::w));
        queue.add(new Path(0, 0, 0, 0, 0, 0, List.of()));

        Map<PointDirection, Integer> min = new HashMap<>();

        for (int l = 0; l < 4; l++) {
            min.put(new PointDirection(0, 0, 0, 0, l), 0);
            min.put(new PointDirection(0, 0, 1, 0, l), 0);
            min.put(new PointDirection(0, 0, 0, 1, l), 0);
            min.put(new PointDirection(0, 0, -1, 0, l), 0);
            min.put(new PointDirection(0, 0, 0, -1, l), 0);
        }


        while (!queue.isEmpty()) {
            Path current = queue.remove();

            //System.out.println(current);

            if (current.i == (height - 1) && current.j == (width - 1)) {
                System.out.println("Found a solution " + current);
                return Integer.toString(current.w);
            }

            List<Path> expanded = current.next(grid, height, width);

            for (var next : expanded) {
                PointDirection pointDirection = new PointDirection(next.i, next.j, next.dI, next.dJ, next.l);
                if (next.w < min.getOrDefault(pointDirection, Integer.MAX_VALUE)) {
                    min.put(pointDirection, next.w);
                    queue.add(next);
                }
            }
        }

        return "0";
    }

    public static String part2(String input) {
        int[][] grid = parseInput(input);
        int height = grid.length;
        int width = grid[0].length;

        PriorityQueue<Path> queue = new PriorityQueue<>(width * height, Comparator.comparingInt(Path::w));
        queue.add(new Path(0, 0, 1, 0, 0, 0, List.of()));
        queue.add(new Path(0, 0, 0, 1, 0, 0, List.of()));

        Map<PointDirection, Integer> min = new HashMap<>();

        for (int l = 0; l < 4; l++) {
            min.put(new PointDirection(0, 0, 1, 0, l), 0);
            min.put(new PointDirection(0, 0, 0, 1, l), 0);
            min.put(new PointDirection(0, 0, -1, 0, l), 0);
            min.put(new PointDirection(0, 0, 0, -1, l), 0);
        }


        while (!queue.isEmpty()) {
            Path current = queue.remove();

            if (current.i == (height - 1) && current.j == (width - 1) && current.l >= 4) {
                System.out.println("Found a solution " + current);
                return Integer.toString(current.w);
            }

            List<Path> expanded = current.nextUltra(grid, height, width);

            for (var next : expanded) {
                PointDirection pointDirection = new PointDirection(next.i, next.j, next.dI, next.dJ, next.l);
                if (next.w < min.getOrDefault(pointDirection, Integer.MAX_VALUE)) {
                    min.put(pointDirection, next.w);
                    queue.add(next);
                }
            }
        }

        return "0";
    }
}
