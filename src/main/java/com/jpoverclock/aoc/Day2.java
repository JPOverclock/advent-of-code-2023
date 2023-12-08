package com.jpoverclock.aoc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {

    record Game(Integer id, List<Map<String, Integer>> plays) {

        static Game parse(String input) {
            String[] parts = input.split(":");

            int id = Integer.parseInt(parts[0].replace("Game ", ""));
            var plays = Arrays.stream(parts[1].split(";")).map(play -> {
                var cubes = play.trim().split(",");
                Map<String, Integer> result = new HashMap<>();

                for (var cube : cubes) {
                    var cubeParts = cube.trim().split(" ");
                    result.put(cubeParts[1].trim(), Integer.parseInt(cubeParts[0].trim()));
                }

                return result;
            }).toList();

            return new Game(id, plays);
        }

        public boolean satisfiesCondition(Map<String, Integer> maxCubes) {
            return plays.stream()
                    .allMatch(play -> play.entrySet().stream()
                            .allMatch(entry -> entry.getValue() <= maxCubes.get(entry.getKey()))
                    );
        }

        public long power() {
            Map<String, Integer> maxCubes = new HashMap<>();

            for (var play : plays) {
                for (var entry : play.entrySet()) {
                    maxCubes.put(entry.getKey(), Math.max(entry.getValue(), maxCubes.getOrDefault(entry.getKey(), 0)));
                }
            }

            Long result = 1L;

            for (var count : maxCubes.values()) {
                result *= count;
            }

            return result;
        }
    }

    public int part1(String input) {
        Map<String, Integer> maxCubes = Map.of(
                "red", 12,
                "green", 13,
                "blue", 14
        );

        return Arrays.stream(input.split("\n"))
                .map(Game::parse)
                .filter(game -> game.satisfiesCondition(maxCubes))
                .mapToInt(Game::id)
                .sum();
    }

    public long part2(String input) {
        return Arrays.stream(input.split("\n"))
                .map(Game::parse)
                .mapToLong(Game::power)
                .sum();
    }
}
