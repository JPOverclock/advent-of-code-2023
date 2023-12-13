package com.jpoverclock.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 {

    private static final boolean DEBUG = false;

    public static class Spring {

        private final String definition;
        private final int[] groups;

        private Spring(String definition, int[] groups) {
            this.definition = definition;
            this.groups = groups;
        }

        public List<Integer> validPositionsForGroup(int startingAt, int group) {
            char[] values = definition.toCharArray();
            List<Integer> validPositions = new LinkedList<>();

            for (int i = startingAt; i <= values.length - group; i++) {
                boolean valid = true;

                if (i == 0) {
                    for (int j = 0; j < group; j++) {
                        if (values[i + j] == '.') {
                            valid = false;
                            break;
                        }
                    }

                    // Edge case, a group cannot be
                    if (values[i + group] == '#') valid = false;
                } else {
                    // General case, check if first position is empty
                    if (values[i - 1] == '#') {
                        valid = false;
                        continue;
                    }

                    for (int j = 0; j < group; j++) {
                        if ((i + j) >= values.length || values[i + j] == '.') {
                            valid = false;
                            break;
                        }
                    }

                    if (i + group == values.length) {
                        // End, to not cound
                    }
                }

                if (valid) {
                    validPositions.add(i);
                }
            }

            return validPositions;
        }

        public boolean isValidPositionSet(List<Integer> positions) {
            char[] definedString = definition.toCharArray();

            for (int i = 0; i < groups.length; i++) {
                int startingPosition = positions.get(i);
                int size = groups[i];

                for (int j = startingPosition, k = 0; k < size; k++, j++) {
                    definedString[j] = '#';
                }
            }

            String[] parts = String.valueOf(definedString).replaceAll("\\?", ".").replaceAll("\\.", " ").trim().split("\\s+");

            if (parts.length != groups.length) return false;

            int[] newGroup = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                newGroup[i] = parts[i].length();
            }

            return Arrays.equals(groups, newGroup);
        }

        public Long validPositions() {
            List<List<Integer>> validPositions = new ArrayList<>();
            char[] values = definition.toCharArray();

            int previousGroup = 0;

            System.out.println("Definition = " + definition);

            for (var group : groups) {
                // A group can only fit on the definition if delimited by . or ? or at the edge

                if (validPositions.isEmpty()) {
                    validPositions = validPositionsForGroup(0, group).stream().map(List::of).toList();
                } else {
                    List<List<Integer>> nextValidPositions = new LinkedList<>();

                    for (List<Integer> positions : validPositions) {
                        List<Integer> morePositions = validPositionsForGroup(positions.get(positions.size() - 1) + previousGroup + 1, group);

                        for (var position : morePositions) {
                            List<Integer> nextList = new LinkedList<>(positions);
                            nextList.add(position);
                            nextValidPositions.add(nextList);
                        }
                    }
                    validPositions = nextValidPositions;
                }

                previousGroup = group;
            }

            // Sanitize valid positions
            validPositions = validPositions.stream().filter(this::isValidPositionSet).toList();

            // DEBUG
            if (DEBUG) {
                System.out.println(definition + " " + Arrays.toString(groups));
                for (List<Integer> positions : validPositions) {
                    char[] debugString = definition.toCharArray();

                    for (int i = 0; i < groups.length; i++) {
                        int startingPosition = positions.get(i);
                        int size = groups[i];

                        for (int j = startingPosition, k = 0; k < size; k++, j++) {
                            debugString[j] = '#';
                        }
                    }

                    System.out.println("\t" + String.copyValueOf(debugString).replaceAll("\\?", "."));
                }
            }

            return (long) validPositions.size();
        }


        public static Spring valueOf(String input) {
            String[] parts = input.split("\\s+");

            int[] groups = Arrays.stream(parts[1].split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            return new Spring(parts[0], groups);
        }

        public static Spring valueOfUnfolded(String input) {
            String[] parts = input.split("\\s+");

            String unfoldedDefinition = IntStream.range(0, 5).mapToObj(i -> parts[0]).collect(Collectors.joining("?"));
            String unfoldedGroups = IntStream.range(0,5).mapToObj(i -> parts[1]).collect(Collectors.joining(","));

            return valueOf(unfoldedDefinition + " " + unfoldedGroups);
        }
    }

    public static String part1(String input) {
        String[] lines = input.split("\n");
        long sum = Arrays.stream(lines)
                .map(Spring::valueOf)
                .mapToLong(Spring::validPositions)
                .sum();

        return Long.toString(sum);
    }

    public static String part2(String input) {
        String[] lines = input.split("\n");
        long sum = Arrays.stream(lines)
                .map(Spring::valueOfUnfolded)
                .mapToLong(Spring::validPositions)
                .sum();

        return Long.toString(sum);
    }
}
