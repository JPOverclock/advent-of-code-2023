package com.jpoverclock.aoc;

import java.util.*;
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

        public List<Integer> validPositionsForGroup(int startingAt, int groupIndex) {
            char[] values = definition.toCharArray();
            int group = groups[groupIndex];
            List<Integer> validPositions = new ArrayList<>();

            int remainingGroupSize = 0;

            for (int i = groupIndex + 1; i < groups.length; i++) {
                remainingGroupSize += (groups[i] + 1);
            }

            for (int i = startingAt; i <= values.length - group - remainingGroupSize; i++) {
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
                        continue;
                    }

                    for (int j = 0; j < group; j++) {
                        if ((i + j) >= values.length || values[i + j] == '.') {
                            valid = false;
                            break;
                        }
                    }

                    if (i + group == values.length) {
                        // End, do not count
                    }
                }

                if (valid) {
                    // Try to see if splitting the string yields the correct group
                    char[] definedString = definition.toCharArray();

                    for (int j = i, k = 0; k < groups[groupIndex]; k++, j++) {
                        definedString[j] = '#';
                    }

                    String[] parts = String.valueOf(definedString).substring(startingAt, i + groups[groupIndex]).replaceAll("\\?", ".").replaceAll("\\.", " ").trim().split("\\s+");

                    if (parts.length == 1 && parts[0].length() == groups[groupIndex]) {
                        validPositions.add(i);
                    }
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

        public boolean isValidPartialPositionSet(List<Integer> positions) {
            char[] definedString = definition.toCharArray();

            for (int i = 0; i < positions.size(); i++) {
                int startingPosition = positions.get(i);
                int size = groups[i];

                for (int j = startingPosition, k = 0; k < size; k++, j++) {
                    definedString[j] = '#';
                }
            }

            String[] parts = String.valueOf(definedString).replaceAll("\\?", ".").replaceAll("\\.", " ").trim().split("\\s+");

            for (int i = 0; i < positions.size(); i++) {
                if (parts[i].length() != groups[i]) return false;
            }

            return true;
        }

        private String positionsToKey(List<Integer> positions) {
            if (positions.isEmpty()) return "R";

            int groupIndex = positions.size() - 1;
            int startingAt = positions.get(groupIndex);

            return "G" + groupIndex + "S" + startingAt;
        }

        public Long validPositionsDfsRecursive(List<Integer> positions, Map<String, Long> memo) {

            //System.out.println(IntStream.range(0, positions.size()).mapToObj(i -> "\t").collect(Collectors.joining()) + Arrays.toString(positions.toArray()));

            if (memo.containsKey(positionsToKey(positions))) return memo.get(positionsToKey(positions));

            if (positions.isEmpty()) {
                // Initial case, root
                return validPositionsForGroup(0, 0).stream()
                        .map(List::of)
                        .filter(this::isValidPartialPositionSet)
                        .mapToLong(p -> validPositionsDfsRecursive(p, memo))
                        .sum();
            } else if (positions.size() == groups.length) {
                // This is a "final" set of positions
                if (isValidPositionSet(positions)) {
                    memo.put(positionsToKey(positions), 1L);
                    return 1L;
                } else {
                    return 0L;
                }
            } else {
                String key = positionsToKey(positions);

                List<Integer> morePositions = validPositionsForGroup(positions.get(positions.size() - 1) + groups[positions.size() - 1] + 1, positions.size());

                long count = 0;

                for (var nextPosition : morePositions) {
                    List<Integer> nextPositions = new ArrayList<>(positions);
                    nextPositions.add(nextPosition);

                    if (isValidPartialPositionSet(nextPositions)) {
                        count += validPositionsDfsRecursive(nextPositions, memo);
                    }
                }

                memo.put(key, count);
                return count;
            }
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
                .mapToLong(spring -> spring.validPositionsDfsRecursive(List.of(), new HashMap<>()))
                .sum();

        return Long.toString(sum);
    }

    public static String part2(String input) {
        String[] lines = input.split("\n");
        long sum = Arrays.stream(lines)
                .map(Spring::valueOfUnfolded)
                .mapToLong(spring -> spring.validPositionsDfsRecursive(List.of(), new HashMap<>()))
                .sum();

        return Long.toString(sum);
    }
}
